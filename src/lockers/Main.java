package lockers;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    LockerRoomCLI cli = new LockerRoomCLI(TestLockerRoom.create(6));

    Scanner scan = new Scanner(System.in);
    while (scan.hasNextLine()) {
      String input = scan.nextLine();

      if ("reset".equals(input)) {
        cli = new LockerRoomCLI(EquinoxLockerRoom.create());
      } else if ("exit".equals(input)) {
        break;
      } else {
        System.out.println(cli.handleInput(input));
      }
    }
  }

}
