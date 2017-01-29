package lockers;


public class LockerUnavailableException extends Exception {

  public LockerUnavailableException(int locker) {
    super("Locker " + locker + " is unavailable");
  }

}
