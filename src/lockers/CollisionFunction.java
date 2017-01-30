package lockers;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * TODO bigpopakap Javadoc this class
 *
 * @author bigpopakap
 * @since 1/29/17
 */
public class CollisionFunction implements Function<Date, Float> {

  public static class CollisionFunctionFactory {

    private final long meanDurationMillis;
    private final long startStdDevMillis;
    private final long endStdDevMillis;

    public CollisionFunctionFactory(long meanDurationMillis, long startStdDevMillis, long endStdDevMillis) {
      this.meanDurationMillis = meanDurationMillis;
      this.startStdDevMillis = startStdDevMillis;
      this.endStdDevMillis = endStdDevMillis;
    }

    public CollisionFunction createNew() {
      return new CollisionFunction(date -> 0f);
    }

    public CollisionFunction createNew(Date start) {
      long startTime = start.getTime();
      long endTime = startTime + meanDurationMillis;

      CollisionFunction startNormal = createNormal(startTime, startStdDevMillis);
      CollisionFunction endNormal = createNormal(endTime, endStdDevMillis);

      return startNormal.add(endNormal);
    }

  }

  private final Function<Date, Float> fn;

  private CollisionFunction(Function<Date, Float> fn) {
    this.fn = fn;
  }

  private static CollisionFunction createNormal(long meanMillis, long stdDevMillis) {
    return new CollisionFunction(dateDate -> {
      long date = dateDate.getTime();
      double result = (1.0 / Math.sqrt(2.0 * stdDevMillis * stdDevMillis * Math.PI))
        * Math.exp(-Math.pow(date - meanMillis, 2) / (2 * stdDevMillis * stdDevMillis));
      return (float) (Math.pow(10, 5)*result);
    });
  }

  @Override
  public Float apply(Date date) {
    return fn.apply(date);
  }

  public CollisionFunction add(CollisionFunction collisionFn) {
    return new CollisionFunction(date -> apply(date) + collisionFn.apply(date));
  }

  public CollisionFunction subtract(CollisionFunction collisionFn) {
    return new CollisionFunction(date -> apply(date) - collisionFn.apply(date));
  }

  public CollisionFunction scale(float scale) {
    return new CollisionFunction(date -> scale * apply(date));
  }

}
