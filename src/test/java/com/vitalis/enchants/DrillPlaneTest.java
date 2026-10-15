package com.vitalis.enchants;

import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrillPlaneTest {

    @Test
    void horizontalPlane() {
        List<int[]> o = DrillListener.plane(BlockFace.UP, true);
        assertEquals(8, o.size());
        for (int[] p : o) {
            assertEquals(0, p[1]);
            assertTrue(p[0] != 0 || p[2] != 0);
        }
    }

    @Test
    void wallPlanes() {
        List<int[]> ns = DrillListener.plane(BlockFace.NORTH, true);
        assertEquals(8, ns.size());
        for (int[] p : ns) assertEquals(0, p[2]);
        List<int[]> ew = DrillListener.plane(BlockFace.EAST, true);
        assertEquals(8, ew.size());
        for (int[] p : ew) assertEquals(0, p[0]);
    }

    @Test
    void fallbackWhenNoRay() {
        List<int[]> o = DrillListener.plane(BlockFace.DOWN, false);
        assertEquals(8, o.size());
        for (int[] p : o) assertEquals(0, p[1]);
    }
}
