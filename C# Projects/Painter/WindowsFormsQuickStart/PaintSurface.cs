using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Windows.Forms;

namespace WindowsFormsQuickStart
{
    public partial class PaintSurface : Form
    {
        private readonly List<Shapes> _shapes = new List<Shapes>();

        private Color _currColor = Color.Black;

        private Point? _pressed;

        private Point? _curr;

        private string _shape = "Line";

        private bool _showPreview;

        public PaintSurface()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void OnKey(object sender, KeyEventArgs e)
        {
            switch (e.KeyCode)
            {
                case Keys.Escape:
                    Close();
                    break;
                case Keys.P:
                    ChangeColor();
                    break;
                case Keys.C:
                    _shapes.Clear();
                    splitContainer1.Panel2.Invalidate();
                    break;
                case Keys.S:
                    _showPreview = !_showPreview;
                    break;
            }
        }

        private void PaintHandler(object sender, PaintEventArgs e)
        {
            var g = e.Graphics;
            foreach (var shape in _shapes)
            {
                shape.drawShape(g);
            }
            if (!_curr.HasValue || !_pressed.HasValue || !_showPreview) return;
            switch (_shape)
            {
                case "Ellipse":
                    new Ellipse(_currColor, _pressed.Value, _curr.Value).drawShape(g);
                    break;
                case "Rectangle":
                    new Rectangle(_currColor, _pressed.Value, _curr.Value).drawShape(g);
                    break;
                default:
                    new Line(_currColor, _pressed.Value, _curr.Value).drawShape(g);
                    break;
            }
        }

        private void OnMouseDown(object sender, MouseEventArgs e)
        {
            _pressed = e.Location;
        }

        private void OnMouseDrag(object sender, MouseEventArgs e)
        {
            if (!_pressed.HasValue) return;

            _curr = e.Location;
            if (!_showPreview) return;
            splitContainer1.Panel2.Invalidate();
        }

        private void OnMouseUp(object sender, MouseEventArgs e)
        {
            Debug.Assert(_pressed != null, "_pressed != null");

            switch (_shape)
            {
                case "Ellipse":
                    _shapes.Add(new Ellipse(_currColor, _pressed.Value, e.Location));
                    break;
                case "Rectangle":
                    _shapes.Add(new Rectangle(_currColor, _pressed.Value, e.Location));
                    break;
                default:
                    _shapes.Add(new Line(_currColor, _pressed.Value, e.Location));
                    break;
            }
            _pressed = null;
            _curr = null;
            splitContainer1.Panel2.Invalidate();
        }

        private void ShapeButtonClicked(object sender, EventArgs e)
        {
            _shape = ((RadioButton) sender).Text;
        }

        private void ChangeColor()
        {
            var result = colorDialog.ShowDialog();

            if (result.Equals(DialogResult.OK))
            {
                _currColor = colorDialog.Color;
            }
        }

        private void ColorChange(object sender, EventArgs e)
        {
            ChangeColor();
            panel1.BackColor = _currColor;
        }
    }
}
