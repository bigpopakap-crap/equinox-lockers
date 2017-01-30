package lockers;

import util.Tuple;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LockerRoom {

  private static final int PROXIMITY_TOLERANCE = 6;

  private LockerGroup allLockers;
  private Set<LockerRequestGroup> lockerRequestGroups;

  public LockerRoom(LockerGroup allLockers, Set<LockerRequestGroup> lockerRequestGroups) {
    this.allLockers = allLockers;
    this.lockerRequestGroups = lockerRequestGroups;
  }

  public Set<LockerRequestGroup> getLockerRequestGroups() {
    return lockerRequestGroups;
  }

  public LockerGroup getAllLockers() {
    return allLockers;
  }

  public List<LockerSuggestion> suggestLockers(LockerRequest request) {
    List<LockerSuggestion> allSuggestions = new ArrayList<>();

    for (LockerGroup acceptableGroup : request.getLockerRequestGroup().getAllGroupsInOrder()) {
      allSuggestions.addAll(suggestLockers(acceptableGroup, request));
    }

    return allSuggestions;
  }

  private List<LockerSuggestion> suggestLockers(LockerGroup acceptableGroup, LockerRequest request) {
    List<LockerSuggestion> suggestions = acceptableGroup.getLockers().stream()
                                            .filter(locker -> locker.isAvailable())
                                            .map(locker -> new LockerSuggestion(acceptableGroup, locker))
                                            .collect(Collectors.<LockerSuggestion>toList());

    Collections.sort(suggestions, (o1, o2) -> {
      float o1Score = o1.getSuggestedLocker().getCollisionFn().apply(request.getDateRequested());
      float o2Score = o2.getSuggestedLocker().getCollisionFn().apply(request.getDateRequested());

      return o2Score > o1Score ? 1 : 0;
    });

    return suggestions;
  }

  public void reserve(int lockerNumber) throws LockerUnavailableException {
    Locker refLocker = findLocker(lockerNumber);
    refLocker.reserve();

    findLockersNear(lockerNumber, PROXIMITY_TOLERANCE).forEach(lockerIntegerTuple -> {
      Locker otherLocker = lockerIntegerTuple.getA();
      int distance = lockerIntegerTuple.getB();

      float scale = (float) (1.0 / distance);

      otherLocker.addCollisionFn(refLocker.getCollisionFn().scale(scale));
    });
  }

  public void release(int lockerNumber) throws LockerUnavailableException {
    Locker refLocker = findLocker(lockerNumber);
    refLocker.release();

    findLockersNear(lockerNumber, PROXIMITY_TOLERANCE).forEach(lockerIntegerTuple -> {
      Locker otherLocker = lockerIntegerTuple.getA();
      int distance = lockerIntegerTuple.getB();

      float scale = (float) (1.0 / distance);

      otherLocker.subtractCollisionFn(refLocker.getCollisionFn().scale(scale));
    });
  }

  public void takeOutOfService(int lockerNumber) throws LockerUnavailableException {
    findLocker(lockerNumber).takeOutOfService();
  }

  public void putInService(int lockerNumber) throws LockerUnavailableException {
    findLocker(lockerNumber).putInService();
  }

  private Locker findLocker(int lockerNumber) throws LockerUnavailableException {
    Optional<Locker> optToReservce = allLockers.getLockers().stream()
        .filter(locker -> locker.getNumber() == lockerNumber).findFirst();
    if (optToReservce.isPresent()) {
      return optToReservce.get();
    } else {
      throw new LockerUnavailableException(lockerNumber);
    }
  }

  private List<Tuple<Locker, Integer>> findLockersNear(int lockerNumber, int tolerance) throws LockerUnavailableException {
    Locker refLocker = findLocker(lockerNumber);

    List<Tuple<Locker, Integer>> lockers = allLockers.getLockers().stream()
                    .map(locker -> new Tuple<>(locker, locker.getPhysicalDistanceTo(refLocker)))
                    .filter(lockerIntegerTuple -> lockerIntegerTuple.getB() <= tolerance)
                    .collect(Collectors.toList());
    Collections.sort(lockers, (o1, o2) -> o2.getB() - o1.getB());

    return lockers;
  }

}
