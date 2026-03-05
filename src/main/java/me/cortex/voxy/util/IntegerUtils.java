package me.cortex.voxy.util;

public class IntegerUtils {
    public static int expand(int i, int mask) {
        // Save original mask
        int originalMask = mask;
        // Count 0's to right
        int maskCount = ~mask << 1;
        int maskPrefix = parallelSuffix(maskCount);
        // Bits to move
        int maskMove1 = maskPrefix & mask;
        // Compress mask
        mask = (mask ^ maskMove1) | (maskMove1 >>> (1 << 0));
        maskCount = maskCount & ~maskPrefix;

        maskPrefix = parallelSuffix(maskCount);
        // Bits to move
        int maskMove2 = maskPrefix & mask;
        // Compress mask
        mask = (mask ^ maskMove2) | (maskMove2 >>> (1 << 1));
        maskCount = maskCount & ~maskPrefix;

        maskPrefix = parallelSuffix(maskCount);
        // Bits to move
        int maskMove3 = maskPrefix & mask;
        // Compress mask
        mask = (mask ^ maskMove3) | (maskMove3 >>> (1 << 2));
        maskCount = maskCount & ~maskPrefix;

        maskPrefix = parallelSuffix(maskCount);
        // Bits to move
        int maskMove4 = maskPrefix & mask;
        // Compress mask
        mask = (mask ^ maskMove4) | (maskMove4 >>> (1 << 3));
        maskCount = maskCount & ~maskPrefix;

        maskPrefix = parallelSuffix(maskCount);
        // Bits to move
        int maskMove5 = maskPrefix & mask;

        int t = i << (1 << 4);
        i = (i & ~maskMove5) | (t & maskMove5);
        t = i << (1 << 3);
        i = (i & ~maskMove4) | (t & maskMove4);
        t = i << (1 << 2);
        i = (i & ~maskMove3) | (t & maskMove3);
        t = i << (1 << 1);
        i = (i & ~maskMove2) | (t & maskMove2);
        t = i << (1 << 0);
        i = (i & ~maskMove1) | (t & maskMove1);

        // Clear irrelevant bits
        return i & originalMask;
    }

    public static int compress(int i, int mask) {
        // See Hacker's Delight (2nd ed) section 7.4 Compress, or Generalized Extract

        i = i & mask; // Clear irrelevant bits
        int maskCount = ~mask << 1; // Count 0's to right

        for (int j = 0; j < 5; j++) {
            // Parallel prefix
            // Mask prefix identifies bits of the mask that have an odd number of 0's to the right
            int maskPrefix = parallelSuffix(maskCount);
            // Bits to move
            int maskMove = maskPrefix & mask;
            // Compress mask
            mask = (mask ^ maskMove) | (maskMove >>> (1 << j));
            // Bits of i to be moved
            int t = i & maskMove;
            // Compress i
            i = (i ^ t) | (t >>> (1 << j));
            // Adjust the mask count by identifying bits that have 0 to the right
            maskCount = maskCount & ~maskPrefix;
        }
        return i;
    }

    private static int parallelSuffix(int maskCount) {
        int maskPrefix = maskCount ^ (maskCount << 1);
        maskPrefix = maskPrefix ^ (maskPrefix << 2);
        maskPrefix = maskPrefix ^ (maskPrefix << 4);
        maskPrefix = maskPrefix ^ (maskPrefix << 8);
        maskPrefix = maskPrefix ^ (maskPrefix << 16);
        return maskPrefix;
    }
}
