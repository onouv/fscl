package fscl.messaging.events;

//import java.util.Arrays;
import java.util.List;

public class ResultDomainEventsAggregate<R, E extends DomainEvent> {

  public final R result;
  public final List<E> events;

  public ResultDomainEventsAggregate(R result, List<E> events) {
    this.result = result;
    this.events = events;
  }
 
}