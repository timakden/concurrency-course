package course.concurrency.exams.auction;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionPessimistic implements Auction {
    private final Notifier notifier;
    private final Lock lock = new ReentrantLock();
    private volatile Bid latestBid = new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE);

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    public boolean propose(Bid bid) {
        if (bid.price > latestBid.price) {
            lock.lock();
            if (bid.price > latestBid.price) {
                notifier.sendOutdatedMessage(latestBid);
                latestBid = bid;
                lock.unlock();
                return true;
            }
            lock.unlock();
        }

        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
