package lockers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lockers.CollisionFunction.CollisionFunctionFactory;

public class LockerGroup {

  private final CollisionFunctionFactory cfFactory;

  private final String name;
  private final Set<Locker> lockers;

  public LockerGroup(CollisionFunctionFactory cfFactory, String name, Set<Locker> lockers) {
    this.cfFactory = cfFactory;
    this.name = name;
    this.lockers = lockers;
  }

  public static LockerGroup fromRange(CollisionFunctionFactory cfFactory, String name, int low, int high) {
    Set<Locker> lockers = new HashSet<>();
    for (int i = low; i <= high; i++) {
      lockers.add(new Locker(cfFactory, i));
    }
    return new LockerGroup(cfFactory, name, lockers);
  }

  public String getName() {
    return name;
  }

  public Set<Locker> getLockers() {
    return lockers;
  }

  public static LockerGroup intersect(CollisionFunctionFactory cfFactory, String name, LockerGroup group1, LockerGroup group2) {
    Set<Locker> newSet = new HashSet<Locker>(group1.getLockers());
    newSet.retainAll(group2.getLockers());
    return new LockerGroup(cfFactory, name, newSet);
  }

  public LockerGroup filterRange(String name, int low, int high) {
    return filter(name, locker -> locker.getNumber() >= low && locker.getNumber() <= high);
  }

  public LockerGroup filter(String name, Predicate<Locker> filter) {
    Set<Locker> newSet = getLockers().stream()
                                    .filter(filter)
                                    .collect(Collectors.<Locker>toSet());
    return new LockerGroup(cfFactory, name, newSet);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LockerGroup that = (LockerGroup) o;

    if (!getName().equals(that.getName())) return false;
    return getLockers().equals(that.getLockers());

  }

  @Override
  public int hashCode() {
    int result = getName().hashCode();
    result = 31 * result + getLockers().hashCode();
    return result;
  }

}
