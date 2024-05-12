// Generated by jextract

package com.nkolosnjaji.webp.gen;

import java.lang.invoke.*;
import java.lang.foreign.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * typedef int (*WebPWriterFunction)(const uint8_t *, size_t, const WebPPicture *)
 * }
 */
public class WebPWriterFunction {

    WebPWriterFunction() {
        // Should not be called directly
    }

    /**
     * The function pointer signature, expressed as a functional interface
     */
    public interface Function {
        int apply(MemorySegment data, long data_size, MemorySegment picture);
    }

    private static final FunctionDescriptor $DESC = FunctionDescriptor.of(
        LibWebP.C_INT,
        LibWebP.C_POINTER,
        LibWebP.C_LONG,
        LibWebP.C_POINTER
    );

    /**
     * The descriptor of this function pointer
     */
    public static FunctionDescriptor descriptor() {
        return $DESC;
    }

    private static final MethodHandle UP$MH = LibWebP.upcallHandle(WebPWriterFunction.Function.class, "apply", $DESC);

    /**
     * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
     * The lifetime of the returned segment is managed by {@code arena}
     */
    public static MemorySegment allocate(WebPWriterFunction.Function fi, Arena arena) {
        return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
    }

    private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

    /**
     * Invoke the upcall stub {@code funcPtr}, with given parameters
     */
    public static int invoke(MemorySegment funcPtr,MemorySegment data, long data_size, MemorySegment picture) {
        try {
            return (int) DOWN$MH.invokeExact(funcPtr, data, data_size, picture);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}

