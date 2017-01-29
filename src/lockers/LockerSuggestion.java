package lockers;

public class LockerSuggestion {

  private final LockerGroup containingGroup;
  private final Locker suggestedLocker;

  public LockerSuggestion(LockerGroup containingGroup, Locker suggestedLocker) {
    this.containingGroup = containingGroup;
    this.suggestedLocker = suggestedLocker;
  }

  public LockerGroup getContainingGroup() {
    return containingGroup;
  }

  public Locker getSuggestedLocker() {
    return suggestedLocker;
  }

  @Override
  public String toString() {
    return "SUGGEST in group " + getContainingGroup().getName()
                + ", locker " + getSuggestedLocker();
  }

}
