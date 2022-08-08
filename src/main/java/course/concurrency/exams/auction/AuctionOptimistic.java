package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {
    private final Notifier notifier;
    private final AtomicReference<Bid> latestBid;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
        latestBid = new AtomicReference<>(new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE));
    }

    public boolean propose(Bid bid) {
        Bid currentBid;
        do {
            currentBid = latestBid.get();
            if (bid.price <= currentBid.price) {
                return false;
            }
        } while (!latestBid.compareAndSet(currentBid, bid));

        notifier.sendOutdatedMessage(currentBid);

        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
