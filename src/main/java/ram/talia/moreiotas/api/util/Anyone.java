package ram.talia.moreiotas.api.util;

import javax.annotation.Nullable;
import java.util.function.Function;

// I rewrote it in java :p

public abstract class Anyone<A,B,C> {
    final boolean isA = false;
    final boolean isB = false;
    final boolean isC = false;

    public @Nullable A a;
    public @Nullable B b;
    public @Nullable C c;

    public abstract <D,E,F> Anyone<D,E,F> map(Function<A, D> aMap, Function<B,E> bMap, Function<C, F> cMap);
    public abstract <D> D flatMap(Function<A, D> aMap, Function<B,D> bMap, Function<C, D> cMap);

    private static class First<A,B,C> extends Anyone<A,B,C>  {
        final boolean isA = true;

        First(A a) {
            this.a = a;
        }

        @Override
        public <D, E, F> Anyone<D, E, F> map(Function<A, D> aMap, Function<B, E> bMap, Function<C, F> cMap) {
            return new First<>(aMap.apply(a));
        }

        @Override
        public <D> D flatMap(Function<A, D> aMap, Function<B, D> bMap, Function<C, D> cMap) {
            return aMap.apply(a);
        }
    }

    private static class Second<A,B,C> extends Anyone<A,B,C>  {
        final boolean isB = true;

        Second(B b) {
            this.b = b;
        }

        @Override
        public <D, E, F> Anyone<D, E, F> map(Function<A, D> aMap, Function<B, E> bMap, Function<C, F> cMap) {
            return new Second<>(bMap.apply(b));
        }

        @Override
        public <D> D flatMap(Function<A, D> aMap, Function<B, D> bMap, Function<C, D> cMap) {
            return bMap.apply(b);
        }
    }

    private static class Third<A,B,C> extends Anyone<A,B,C>  {
        final boolean isC = true;

        Third(C c) {
            this.c = c;
        }

        @Override
        public <D, E, F> Anyone<D, E, F> map(Function<A, D> aMap, Function<B, E> bMap, Function<C, F> cMap) {
            return new Third<>(cMap.apply(c));
        }

        @Override
        public <D> D flatMap(Function<A, D> aMap, Function<B, D> bMap, Function<C, D> cMap) {
            return cMap.apply(c);
        }
    }

    public static <A,B,C> Anyone<A,B,C> first(A contents) {return new First<>(contents);}
    public static <A,B,C> Anyone<A,B,C> second(B contents) {return new Second<>(contents);}
    public static <A,B,C> Anyone<A,B,C> third(C contents) {return new Third<>(contents);}
}


