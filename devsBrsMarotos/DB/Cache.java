/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;

import devsBrsMarotos.DB.Models.PlayerData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author vntgasl
 */
public class Cache<Type> {

    private HashMap<Object, Type> cache = new HashMap<Object, Type>();

    public void set(Object u, Type obj) {
        cache.put(u, obj);
    }

    public boolean hasCache(Object u) {
        return cache.containsKey(u);
    }

    public int size() {
        return cache.size();
    }

    public void remove(Object u) {
        cache.remove(u);
    }

    public Collection<Type> getCached() {
        return cache.values();
    }

    public Type getCached(Object u) {
        if (cache.containsKey(u)) {
            return cache.get(u);
        } else {
            return null;
        }
    }

}
