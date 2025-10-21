package fr.fruityhedgeh0g.model.dtos;

public interface Views {
    interface IdentityOnly {}
    interface System extends IdentityOnly {}
    interface Minimal extends IdentityOnly {}
    interface Basic extends Minimal{}
    interface Full extends Basic{}
    interface Creation {}
}

