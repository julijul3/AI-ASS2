/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Raquel
 */
public interface Bool {

    boolean get();

    default Bool and(Bool r) {
        return () -> get() ? r.get() : false;
    }

    default Bool or(Bool r) {
        return () -> get() ? true : r.get();
    }

    default Bool not() {
        return () -> !get();
    }
}
