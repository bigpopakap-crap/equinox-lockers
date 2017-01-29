package lockers;

import java.util.Date;

public class Locker {

  private final int number;
  private boolean isOutOfService = false;
  private Date timeReserved = null;

  public Locker(int number, boolean isOutOfService) {
    this.number = number;
    this.isOutOfService = isOutOfService;
  }

  public Locker(int number) {
    this(number, false);
  }

  public int getNumber() {
    return number;
  }

  public boolean isLower() {
    return number % 2 == 0;
  }

  public Date getTimeReserved() {
    return timeReserved;
  }

  public boolean isReserved() {
    return getTimeReserved() != null;
  }

  public boolean isAvailable() {
    return !isOutOfService && !isReserved();
  }

  public boolean isInSameColumn(Locker otherLocker) {
    if (isLower()) {
      return getNumber() - 1 == otherLocker.getNumber();
    } else {
      return getNumber() + 1 == otherLocker.getNumber();
    }
  }

  public int getCollisionScore() {
    return 0;
    // TODO actually keep track of these
  }

  private int getPhysicalDistanceTo(Locker otherLocker) {
    /*
      Approximate the physical distance by the difference in locker number.
      But special case for when they're right on top of each other
     */
    return isInSameColumn(otherLocker) ? 0
              : Math.abs(getNumber() - otherLocker.getNumber());
  }

  public void reserve() throws LockerUnavailableException {
    reserve(new Date());
  }

  public void reserve(Date timeReserved) throws LockerUnavailableException {
    if (!isAvailable()) {
      throw new LockerUnavailableException(getNumber());
    }
    this.timeReserved = timeReserved;
  }

  public void release() {
    this.timeReserved = null;
  }

  public void takeOutOfService() throws LockerUnavailableException {
    if (isReserved()) {
      throw new LockerUnavailableException(getNumber());
    }

    this.isOutOfService = true;
  }

  public void putInService() {
    this.isOutOfService = false;
  }

  @Override
  public String toString() {
    return "Locker " + getNumber()
                    + " (collision score " + getCollisionScore() + ")"
                    + (isReserved() ? " (reserved at " + getTimeReserved() + ")" : "")
                    + (isOutOfService ? " (out of service)" : "");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Locker locker = (Locker) o;

    if (getNumber() != locker.getNumber()) return false;
    if (isOutOfService != locker.isOutOfService) return false;
    return getTimeReserved() != null ? getTimeReserved().equals(locker.getTimeReserved()) : locker.getTimeReserved() == null;
  }

  @Override
  public int hashCode() {
    int result = getNumber();
    result = 31 * result + (isOutOfService ? 1 : 0);
    result = 31 * result + (getTimeReserved() != null ? getTimeReserved().hashCode() : 0);
    return result;
  }

}
