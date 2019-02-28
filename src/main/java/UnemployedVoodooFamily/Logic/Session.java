package UnemployedVoodooFamily.Logic;

import ch.simas.jtoggl.JToggl;

public class Session {

    private static JToggl jToggl = null;
    private static Session ourInstance = new Session();

    public static Session getInstance() {
        return ourInstance;
    }

    private Session() {
    }

    public void setSession(JToggl jToggl) {
        if(this.jToggl == null) {
            this.jToggl = jToggl;
        }
        else {
            //do something
        }
    }

    public JToggl getSession() {
        return this.jToggl;
    }

    public void terminateSession() {
        jToggl = null;
    }
}
