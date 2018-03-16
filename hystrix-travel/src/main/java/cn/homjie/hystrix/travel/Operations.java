package cn.homjie.hystrix.travel;

import java.util.concurrent.TimeUnit;

/**
 * @author jiehong.jh
 * @date 2018/3/16
 */
public abstract class Operations {

    private static final Operable NOTHING = new NothingOperation();
    private static final Operable EXCEPTION = new ExceptionOperation();

    public static Operable nothing() {
        return NOTHING;
    }

    public static Operable sleep(long timeout) {
        return new SleepOperation(timeout);
    }

    public static Operable exception() {
        return EXCEPTION;
    }

    public static Operable exception(Exception ex) {
        return new ExceptionOperation(ex);
    }

    private static class NothingOperation implements Operable {
        @Override
        public void execute() throws Exception {
            // do nothing
        }
    }

    private static class SleepOperation implements Operable {
        private long timeout;

        private SleepOperation(long timeout) {this.timeout = timeout;}

        @Override
        public void execute() throws Exception {
            TimeUnit.MILLISECONDS.sleep(timeout);
        }
    }

    private static class ExceptionOperation implements Operable {
        private Exception exception;

        private ExceptionOperation(Exception exception) {this.exception = exception;}

        private ExceptionOperation() {this(new Exception("operation default exception"));}

        @Override
        public void execute() throws Exception {
            throw exception;
        }
    }

}
