package lockers;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import lockers.CollisionFunction.CollisionFunctionFactory;

public class Main {

  public static void main(String[] args) {
    CollisionFunctionFactory cfFactory = new CollisionFunctionFactory(
      TimeUnit.SECONDS.toMillis(60),
      TimeUnit.SECONDS.toMillis(5),
      TimeUnit.SECONDS.toMillis(10)
    );
    LockerRoomCLI cli = new LockerRoomCLI(TestLockerRoom.create(cfFactory, 6));

    Scanner scan = new Scanner(System.in);
    while (scan.hasNextLine()) {
      String input = scan.nextLine();

      if ("reset".equals(input)) {
        cli = new LockerRoomCLI(EquinoxLockerRoom.create(cfFactory));
      } else if ("exit".equals(input)) {
        break;
      } else {
        System.out.println(cli.handleInput(input));
      }
    }
  }

}
