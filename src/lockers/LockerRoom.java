package lockers;

import java.util.*;
import java.util.stream.Collectors;

public class LockerRoom {

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

    Collections.sort(suggestions, (o1, o2) ->
        o2.getSuggestedLocker().getCollisionScore() - o1.getSuggestedLocker().getCollisionScore());

    return suggestions;
  }

  public void reserve(int lockerNumber) throws LockerUnavailableException {
    findLocker(lockerNumber).reserve();
  }

  public void release(int lockerNumber) throws LockerUnavailableException {
    findLocker(lockerNumber).release();
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

}
