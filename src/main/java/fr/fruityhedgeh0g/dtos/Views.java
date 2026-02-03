package fr.fruityhedgeh0g.dtos;

public class Views {
    //Creation DTO types
    public static class Creation {}

    public static class CreationResponse extends Creation {}

    //Response DTO types
    public static class Update {}

    public static class UpdateResponse extends Update {}

    //Read DTO types
    public static class Minimal {}

    public static class Basic extends Minimal{}

    public static class Detailed extends Basic {}


}
