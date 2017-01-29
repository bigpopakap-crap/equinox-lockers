package lockers;

import java.util.Date;

public class LockerRequest {

  private final LockerRequestGroup requestGroup;
  private final Date dateRequested;

  public LockerRequest(LockerRequestGroup requestGroup) {
    this(requestGroup, new Date());
  }

  public LockerRequest(LockerRequestGroup requestGroup, Date dateRequested) {
    this.requestGroup = requestGroup;
    this.dateRequested = dateRequested;
  }

  public LockerRequestGroup getLockerRequestGroup() {
    return requestGroup;
  }

  public Date getDateRequested() {
    return dateRequested;
  }

}
