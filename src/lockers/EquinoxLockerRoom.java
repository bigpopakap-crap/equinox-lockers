package lockers;

import java.util.HashSet;
import java.util.Set;

public class EquinoxLockerRoom {

  public static LockerRoom create() {
    LockerGroup allLockers = LockerGroup.fromRange("any", 0, 400);

    LockerGroup leftShowers = allLockers.filterRange("left showers", 0, 50);
    LockerGroup rightShowers = allLockers.filterRange("right showers", 51, 100);
    LockerGroup leftMid = allLockers.filterRange("left mid", 101, 150);
    LockerGroup rightMid = allLockers.filterRange("right mid", 151, 200);
    LockerGroup leftClose = allLockers.filterRange("left close", 201, 250);
    LockerGroup rightClose = allLockers.filterRange("right close", 251, 300);
    LockerGroup pool = allLockers.filterRange("pool", 301, 350);
    LockerGroup entrance = allLockers.filterRange("entrance", 351, 400);

    LockerRequestGroup anyRG = new LockerRequestGroup(allLockers);

    LockerRequestGroup leftShowersRG = new LockerRequestGroup(leftShowers,
        rightShowers, leftMid, rightMid, leftClose,
        rightClose, pool, entrance);

    LockerRequestGroup rightShowersRG = new LockerRequestGroup(rightShowers,
        leftShowers, rightMid, leftMid, rightClose,
        leftClose, pool, entrance);

    LockerRequestGroup leftMidRG = new LockerRequestGroup(leftMid,
        leftShowers, rightMid, rightShowers, leftClose,
        rightClose, pool, entrance);

    LockerRequestGroup rightMidRG = new LockerRequestGroup(rightMid,
        rightShowers, leftMid, leftShowers, leftClose,
        rightClose, pool, entrance);

    LockerRequestGroup leftCloseRG = new LockerRequestGroup(leftClose,
        rightClose, leftMid, rightMid, leftShowers,
        rightShowers, pool, entrance);

    LockerRequestGroup rightCloseRG = new LockerRequestGroup(rightClose,
        leftClose, rightMid, leftMid, rightShowers,
        leftShowers, pool, entrance);

    LockerRequestGroup poolRG = new LockerRequestGroup(pool,
        rightClose, entrance, leftClose, rightMid,
        leftMid, rightShowers, leftShowers);

    LockerRequestGroup entranceRG = new LockerRequestGroup(entrance,
        pool, rightClose, leftClose, rightMid,
        leftMid, rightShowers, leftShowers);

    Set<LockerRequestGroup> requestGroups = new HashSet<>();
    requestGroups.add(anyRG);
    requestGroups.add(leftShowersRG);
    requestGroups.add(rightShowersRG);
    requestGroups.add(leftMidRG);
    requestGroups.add(rightMidRG);
    requestGroups.add(leftCloseRG);
    requestGroups.add(rightCloseRG);
    requestGroups.add(poolRG);
    requestGroups.add(entranceRG);

    return new LockerRoom(allLockers, requestGroups);
  }

}
