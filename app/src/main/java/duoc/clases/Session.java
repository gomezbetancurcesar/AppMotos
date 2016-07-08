package duoc.clases;

public final class Session {
    private static int id;

    public void escribirSession(int idUsuario){
        id = idUsuario;
    }

    public int getId(){
        return id;
    }
}
