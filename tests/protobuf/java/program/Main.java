package program;

import proto.AddressBookProtos2;
import proto.AddressBookProtos3;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
  public static void main(String args[]) {
    try {
      AddressBookProtos2.Person john = AddressBookProtos2.Person.newBuilder()
        .setId(1234)
        .setName("John Doe")
        .setEmail("jdoe@example.com")
        .addPhone(
          AddressBookProtos2.Person.PhoneNumber.newBuilder()
            .setNumber("555-4321")
            .setType(AddressBookProtos2.Person.PhoneType.HOME))
        .build();
      String filename = "out";

      FileOutputStream output = new FileOutputStream(filename);

      john.writeTo(output);

      output.close();

      AddressBookProtos3.Person john3 = AddressBookProtos3.Person.parseFrom(new FileInputStream(filename));

      System.out.println("Person ID: " + john3.getId());
      System.out.println("Name : " + john3.getName());
      System.out.println("Email : " + john3.getEmail());

      System.out.println(john3.getPhone().getType());
      System.out.println(john3.getPhone().getNumber());
    } catch (Exception e) {
    }
      /*
      switch (phoneNumber.getType()) {

        case AddressBookProtos3.Person.MOBILE:
          System.out.print("  Mobile phone #: ");
          break;
        case AddressBookProtos3.Person.HOME:
          System.out.print("  Home phone #: ");
          break;
        case AddressBookProtos3.Person.WORK:
          System.out.print("  Work phone #: ");
          break;
      }
      */
  }
}

