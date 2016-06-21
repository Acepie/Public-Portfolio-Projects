package cs3500.music.controller.events;

/**
 * Mock Runnable for testing purposes
 */
public class MockRunnable implements Runnable {

  StringBuilder stringBuilder;

  public MockRunnable(StringBuilder stringBuilder) {
    this.stringBuilder = stringBuilder;
  }

  @Override
  public void run() {
    stringBuilder.append("Mock runnable has been run\n");
  }

  @Override
  public String toString() {
    return "MockRunnable";
  }
}
