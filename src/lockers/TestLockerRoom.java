package lockers;

import java.util.HashSet;
import java.util.Set;

import lockers.CollisionFunction.CollisionFunctionFactory;

public class TestLockerRoom {

  public static LockerRoom create(CollisionFunctionFactory cfFactory, int size) {
    LockerGroup allLockers = LockerGroup.fromRange(cfFactory, "any", 1, size);

    LockerGroup left = allLockers.filterRange("left", 1, size / 3);
    LockerGroup mid = allLockers.filterRange("mid", size/3 + 1, size / 2);
    LockerGroup right = allLockers.filterRange("right", size/2 + 1, size);

    LockerRequestGroup anyRG = new LockerRequestGroup(allLockers);
    LockerRequestGroup leftRG = new LockerRequestGroup(left, mid, right);
    LockerRequestGroup midRG = new LockerRequestGroup(mid, left, right);
    LockerRequestGroup rightRG = new LockerRequestGroup(right, mid, left);

    Set<LockerRequestGroup> requestGroups = new HashSet<>();
    requestGroups.add(anyRG);
    requestGroups.add(leftRG);
    requestGroups.add(midRG);
    requestGroups.add(rightRG);

    return new LockerRoom(allLockers, requestGroups);
  }

}
