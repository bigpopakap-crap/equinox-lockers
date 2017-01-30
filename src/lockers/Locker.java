package lockers;

import java.util.Date;
import lockers.CollisionFunction.CollisionFunctionFactory;

public class Locker {

  private final CollisionFunctionFactory cfFactory;

  private final int number;
  private CollisionFunction collisionFn;
  private boolean isOutOfService = false;
  private Date timeReserved = null;

  public Locker(CollisionFunctionFactory cfFactory, int number, boolean isOutOfService) {
    this.cfFactory = cfFactory;
    this.number = number;
    this.isOutOfService = isOutOfService;

    this.collisionFn = this.cfFactory.createNew();
  }

  public Locker(CollisionFunctionFactory cfFactory, int number) {
    this(cfFactory, number, false);
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

  public CollisionFunction getCollisionFn() {
    return collisionFn;
  }

  public void addCollisionFn(CollisionFunction fn) {
    this.collisionFn = this.collisionFn.add(fn);
  }

  public void subtractCollisionFn(CollisionFunction fn) {
    this.collisionFn = this.collisionFn.subtract(fn);
  }

  public int getPhysicalDistanceTo(Locker otherLocker) {
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
    this.collisionFn = cfFactory.createNew(timeReserved);
    this.timeReserved = timeReserved;
  }

  public void release() {
    this.collisionFn = cfFactory.createNew();
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
                    + " (collision score " + getCollisionFn().apply(new Date()) + ")"
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
