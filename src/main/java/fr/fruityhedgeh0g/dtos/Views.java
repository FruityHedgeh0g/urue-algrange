package fr.fruityhedgeh0g.dtos;

public class Views {
    //Creation DTO types
    public interface Creation {}

    public interface CreationResponse extends Creation {}

    //Response DTO types
    public interface Update {}

    public interface UpdateResponse extends Update {}

    //Read DTO types
    public interface Minimal {}

    public interface Basic extends Minimal{}

    public interface Detailed extends Basic {}


}
