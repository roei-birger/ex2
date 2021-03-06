
import api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class NodeDataTest {
    NodeData n;
    static long start = new Date().getTime();

    @BeforeAll
    public static void BeforeAll() {
        System.out.println("Start");
    }

    //reset node to default
    @BeforeEach
    public void BeforeEach() {
        n = node_creator();
    }

    @Test
    void getKey() {
        assertEquals(5, n.getKey(), "getKey didn't return the correct key");
    }

    @Test
    void getLocation() {
                assertEquals(new location(1,1,1),n.getLocation(),"wrong location");
            }

    @Test
    void setLocation() {
        n.setLocation(new location(0,0,1));
        assertEquals(new location(0,0,1),n.getLocation(),"location couldn't update successfully");
    }

    @Test
    void getWeight() {
        assertEquals(1.1, n.getWeight(), "getWeight didn't return the correct weight");
    }

    @Test
    void setWeight() {
        n.setWeight(4.2);
        assertEquals(4.2, n.getWeight(), "setWeight couldn't update successfully");

    }

    @Test
    void getInfo() {
        assertEquals("b", n.getInfo(), "getInfo didn't return the correct info");
    }

    @Test
    void setInfo() {
        n.setInfo("c");
        assertEquals("c", n.getInfo(), "setInfo didn't update successfully");
        n.setInfo(null);
        assertNull(n.getInfo(), "setInfo didn't return Null");
    }

    @Test
    void getTag() {
        assertEquals(1, n.getTag(), "getTag didn't return the correct tag");
    }

    @Test
    void setTag() {
        n.setTag(2);
        assertEquals(2, n.getTag(), "setTag didn't update successfully");
    }

    @Test
    void getNi() {
        assertEquals(2,n.getNi().size(),  "getNi didn't return the correct collection");
    }

    @Test
    void getEdge() {
        assertEquals(4.4,n.getEdge(2).getWeight(),"getEdge didn't return the correct edge");
    }

    @Test
    void getEd() {
        assertEquals(2,n.getEd().size(),  "getEd didn't return the correct collection");
    }

    @Test
    void hasNi() {
        assertTrue(n.hasNi(2),"hasNi didn't return the correct answer");
        assertTrue(n.hasNi(4),"hasNi didn't return the correct answer");
    }

    @Test
    void addNi() {
        n.addNi(new NodeData(6), 0.0);
        assertTrue(n.hasNi(6),"addNi couldn't update successfully");

    }

    @Test
    void removeNode() {
       NodeData temp =new NodeData(6);
       n.addNi(temp,0.8);
       n.removeNode(temp);
       assertFalse(n.hasNi(6),"removeNode couldn't update successfully");
    }

    @AfterAll
    public static void AfterAll() {
        System.out.println("Finish succeed");
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        System.out.println("All program runTime: " + dt);
    }

    public NodeData node_creator() {
        NodeData n = new NodeData(5);

        node_data n1 = new NodeData(2);
        node_data n2 = new NodeData(4);
        n.addNi(n1,4.4);
        n.addNi(n2,9.4);
        n.setTag(1);
        n.setWeight(1.1);
        n.setInfo("b");
        n.setLocation(new location(1,1,1));


        return n;
    }
}