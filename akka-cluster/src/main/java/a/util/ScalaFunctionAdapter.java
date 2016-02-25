package a.util;

import akka.japi.JavaPartialFunction;
import com.ajjpj.afoundation.function.AFunction0;
import com.ajjpj.afoundation.function.AFunction1;
import com.ajjpj.afoundation.function.AStatement1;
import com.ajjpj.afoundation.util.AUnchecker;
import scala.Function0;
import scala.Function1;
import scala.PartialFunction;
import scala.runtime.AbstractFunction0;
import scala.runtime.AbstractFunction1;

import java.util.Arrays;

public class ScalaFunctionAdapter {

    @SuppressWarnings({"unchecked", "UnusedParameters"})
    public static <P, R> Function1<P, R> castTo (Class<R> clazz) {
        return func(x -> (R) x);
    }

    public static <P, R> Function0<R> func (AFunction0<R, Exception> f) {
        return new AbstractFunction0<R> () {
            @Override public R apply () {
                try {
                    return f.apply();
                }
                catch(Exception e) {
                    AUnchecker.throwUnchecked(e);
                    return null;
                }
            }
        };
    }

    public static <P, R> Function1<P, R> func (AFunction1<P, R, Exception> f) {
        return new AbstractFunction1<P, R>() {
            @Override public R apply (P o) {
                try {
                    return f.apply(o);
                }
                catch(Exception e) {
                    AUnchecker.throwUnchecked(e);
                    return null;
                }
            }
        };
    }

    public static <P, R> Function1<P, R> func (Case... cases) {
        return new AbstractFunction1<P, R>() {
            @Override public R apply (P o) {
                try {
                    for(Case c : cases) {
                        if(c.cls.isInstance(o)) {
                            //noinspection unchecked
                            return (R) ((Case<P>)c).f.apply(o);
                        }
                    }
                    throw JavaPartialFunction.noMatch();
                }
                catch(Exception e) {
                    AUnchecker.throwUnchecked(e);
                    return null;
                }
            }
        };
    }

    public static <P, R> PartialFunction<P, R> partFunc (Case... cases) {
        return new JavaPartialFunction<P, R>() {
            @Override public R apply (P o, boolean isCheck) throws Exception {
                if(isCheck) {
                    if(Arrays.stream(cases).anyMatch(x -> x.cls.isInstance(o))) return null;
                }
                else {
                    for(Case c : cases) {
                        if(c.cls.isInstance(o)) {
                            //noinspection unchecked
                            return (R) ((Case<P>)c).f.apply(o);
                        }
                    }
                }
                throw JavaPartialFunction.noMatch();
            }
        };
    }

    public static <P> Case<P> fCase (Class<P> cls, AFunction1<P, ?, Exception> f) {
        return new Case<>(cls, f::apply);
    }

    public static <P> Case<P> voidCase (Class<P> cls, AStatement1<P, Exception> f) {
        return new Case<>(cls, o -> {
            f.apply(o);
            return null;
        });
    }

    public static class Case<P> {
        public final Class<P> cls;
        public final AFunction1<P, Object, Exception> f;

        public Case (Class<P> cls, AFunction1<P, Object, Exception> f) {
            this.cls = cls;
            this.f = f;
        }
    }

}
