using System;
using UnityEngine;
using System.Collections;

public class PlayerController : MonoBehaviour
{
    public static readonly Vector3 startPosition = new Vector3(-11, 2, 10);

    private bool facingRight = true;

    private bool jump = false;

    public float acceleration = 100;
    public float maxSpeed = 5;
    public float jumpPow = 1000;

    public Transform GroundTransform;
    public Transform LeftTransform;
    public Transform RightTransform;

    private bool grounded = false;
    private RaycastHit2D walled;
    private RaycastHit2D? lastWall;
    private Animator anim;
    private Rigidbody2D rb;

    public int MaxJumpCount = 1;
    private int JumpCount = 0;

    private enum State
    {
        Idle,
        Running,
        Jumping,
        Sliding
    }

    private State state;

	// Use this for initialization
	void Start ()
	{
	    anim = GetComponent<Animator>();
	    rb = GetComponent<Rigidbody2D>();
	}
	
	// Update is called once per frame
	void Update ()
	{
	    grounded = Physics2D.Linecast(transform.position, GroundTransform.position, 1 << LayerMask.NameToLayer("Floor"));

	    walled = Physics2D.Linecast(transform.position, LeftTransform.position, 1 << LayerMask.NameToLayer("Floor"));

	    if (!walled)
	    {
            walled = Physics2D.Linecast(transform.position, RightTransform.position, 1 << LayerMask.NameToLayer("Floor"));
        }

        if (grounded)
        {
            lastWall = null;

	        if (state == State.Jumping || state == State.Sliding)
	        {
                state = State.Idle;
                anim.SetTrigger("Idle");
            }

	        JumpCount = 0;
	    } else if (walled && state == State.Jumping)
	    {
	        state = State.Sliding;
            anim.SetTrigger("Sliding");

	        if (lastWall.HasValue && !(lastWall.Equals(walled)))
            {
                JumpCount = MaxJumpCount - 1;
            }

            lastWall = walled;
	    }

	    if (!Input.GetButtonDown("Jump") || JumpCount >= MaxJumpCount)
	    {
	        return;
	    }

	    JumpCount++;
	    jump = true;
	    state = State.Jumping;
	}

    void FixedUpdate()
    {
        float speed = Input.GetAxis("Horizontal");

        if (Mathf.Abs(speed) > 0 && state == State.Idle)
        {
            state = State.Running;
            anim.SetTrigger("Running");
        } else if (state == State.Running && !(Mathf.Abs(speed) > 0))
        {
            state = State.Idle;
            anim.SetTrigger("Idle");
        }
        
        if (speed*rb.velocity.x < maxSpeed)
        {
            rb.AddForce(Vector2.right * speed * acceleration);
        }

        if (Mathf.Abs(rb.velocity.x) > maxSpeed)
        {
            rb.velocity = new Vector2(Mathf.Sign(rb.velocity.x) * maxSpeed, rb.velocity.y);
        }

        if ((speed > 0 && !facingRight) || (speed < 0 && facingRight))
        {
            Flip();
        }

        if (jump)
        {
            state = State.Jumping;
            anim.SetTrigger("Jumping");
            rb.AddForce(Vector2.up * jumpPow);
            jump = false;
        }
    }

    void Flip()
    {
        facingRight = !facingRight;
        Vector3 scale = transform.localScale;
        scale.x *= -1;
        transform.localScale = scale;
    }

    void OnTriggerEnter2D(Collider2D other)
    {
        if (other.gameObject.layer == LayerMask.NameToLayer("Death"))
        {
            transform.position = startPosition;
        }
    }
}
