package cs3500.music.controller.events;

/**
 * Mock Mouse runnable for testing purposes
 */
public class MockMouseRunnable implements MouseRunnable {

  StringBuilder stringBuilder;

  public MockMouseRunnable(StringBuilder stringBuilder) {
    this.stringBuilder = stringBuilder;
  }

  @Override
  public void run(int x, int y) {
    stringBuilder.append("Mock mouse runnable has been run with coordinates:");
    stringBuilder.append(x);
    stringBuilder.append(", ");
    stringBuilder.append(y);
    stringBuilder.append('\n');
  }

  @Override
  public String toString() {
    return "MockMouseRunnable";
  }
}
