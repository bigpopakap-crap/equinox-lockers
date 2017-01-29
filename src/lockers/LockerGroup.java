package lockers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LockerGroup {

  private final String name;
  private final Set<Locker> lockers;

  public LockerGroup(String name, Set<Locker> lockers) {
    this.name = name;
    this.lockers = lockers;
  }

  public static LockerGroup fromRange(String name, int low, int high) {
    Set<Locker> lockers = new HashSet<>();
    for (int i = low; i <= high; i++) {
      lockers.add(new Locker(i));
    }
    return new LockerGroup(name, lockers);
  }

  public String getName() {
    return name;
  }

  public Set<Locker> getLockers() {
    return lockers;
  }

  public static LockerGroup intersect(String name, LockerGroup group1, LockerGroup group2) {
    Set<Locker> newSet = new HashSet<Locker>(group1.getLockers());
    newSet.retainAll(group2.getLockers());
    return new LockerGroup(name, newSet);
  }

  public LockerGroup filterRange(String name, int low, int high) {
    return filter(name, locker -> locker.getNumber() >= low && locker.getNumber() <= high);
  }

  public LockerGroup filter(String name, Predicate<Locker> filter) {
    Set<Locker> newSet = getLockers().stream()
                                    .filter(filter)
                                    .collect(Collectors.<Locker>toSet());
    return new LockerGroup(name, newSet);
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
