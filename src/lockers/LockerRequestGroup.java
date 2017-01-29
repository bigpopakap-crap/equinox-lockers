package lockers;

import java.util.ArrayList;
import java.util.List;

public class LockerRequestGroup {

  private final LockerGroup primaryGroup;
  private final List<LockerGroup> allGroupsInOrder;

  public LockerRequestGroup(LockerGroup... lockerGroups) {
    this.primaryGroup = lockerGroups[0];

    this.allGroupsInOrder = new ArrayList<>();
    for (LockerGroup group : lockerGroups) {
      this.allGroupsInOrder.add(group);
    }
  }

  public LockerGroup getPrimaryGroup() {
    return primaryGroup;
  }

  public List<LockerGroup> getAllGroupsInOrder() {
    return allGroupsInOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LockerRequestGroup that = (LockerRequestGroup) o;

    if (getPrimaryGroup() != null ? !getPrimaryGroup().equals(that.getPrimaryGroup()) : that.getPrimaryGroup() != null)
      return false;
    return !(getAllGroupsInOrder() != null ? !getAllGroupsInOrder().equals(that.getAllGroupsInOrder()) : that.getAllGroupsInOrder() != null);

  }

  @Override
  public int hashCode() {
    int result = getPrimaryGroup() != null ? getPrimaryGroup().hashCode() : 0;
    result = 31 * result + (getAllGroupsInOrder() != null ? getAllGroupsInOrder().hashCode() : 0);
    return result;
  }

}
