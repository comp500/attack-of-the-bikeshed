Subject: A bike shed (any colour will do) on greener grass...  
From: Poul-Henning Kamp <phk@freebsd.org>  
Date: Sat, 02 Oct 1999 16:14:10 +0200  
Message-ID: <18238.938873650@critter.freebsd.dk>  
Sender: phk@critter.freebsd.dk  
Bcc: Blind Distribution List: ;  
MIME-Version: 1.0


[bcc'ed to committers, hackers]

My last pamphlet was sufficiently well received that I was not
scared away from sending another one, and today I have the time
and inclination to do so.

I've had a little trouble with deciding on the right distribution
of this kind of stuff, this time it is bcc'ed to committers and
hackers, that is probably the best I can do.  I'm not subscribed
to hackers myself but more on that later.

The thing which have triggered me this time is the "sleep(1) should
do fractional seconds" thread, which have pestered our lives for
many days now, it's probably already a couple of weeks, I can't
even be bothered to check.

To those of you who have missed this particular thread: Congratulations.

It was a proposal to make sleep(1) DTRT if given a non-integer
argument that set this particular grass-fire off.  I'm not going
to say anymore about it than that, because it is a much smaller
item than one would expect from the length of the thread, and it
has already received far more attention than some of the *problems*
we have around here.

The sleep(1) saga is the most blatant example of a bike shed
discussion we have had ever in FreeBSD.  The proposal was well
thought out, we would gain compatibility with OpenBSD and NetBSD,
and still be fully compatible with any code anyone ever wrote.

Yet so many objections, proposals and changes were raised and
launched that one would think the change would have plugged all
the holes in swiss cheese or changed the taste of Coca Cola or
something similar serious.

"What is it about this bike shed ?" Some of you have asked me.

It's a long story, or rather it's an old story, but it is quite
short actually.  C. Northcote Parkinson wrote a book in the early
1960'ies, called "Parkinson's Law", which contains a lot of insight
into the dynamics of management.

You can find it on Amazon, and maybe also in your dads book-shelf,
it is well worth its price and the time to read it either way,
if you like Dilbert, you'll like Parkinson.

Somebody recently told me that he had read it and found that only
about 50% of it applied these days.  That is pretty darn good I
would say, many of the modern management books have hit-rates a
lot lower than that, and this one is 35+ years old.

In the specific example involving the bike shed, the other vital
component is an atomic power-plant, I guess that illustrates the
age of the book.

Parkinson shows how you can go in to the board of directors and
get approval for building a multi-million or even billion dollar
atomic power plant, but if you want to build a bike shed you will
be tangled up in endless discussions.

Parkinson explains that this is because an atomic plant is so vast,
so expensive and so complicated that people cannot grasp it, and
rather than try, they fall back on the assumption that somebody
else checked all the details before it got this far.   Richard P.
Feynmann gives a couple of interesting, and very much to the point,
examples relating to Los Alamos in his books.

A bike shed on the other hand.  Anyone can build one of those over
a weekend, and still have time to watch the game on TV.  So no
matter how well prepared, no matter how reasonable you are with
your proposal, somebody will seize the chance to show that he is
doing his job, that he is paying attention, that he is *here*.

In Denmark we call it "setting your fingerprint".  It is about
personal pride and prestige, it is about being able to point
somewhere and say "There!  *I* did that."  It is a strong trait in
politicians, but present in most people given the chance.  Just
think about footsteps in wet cement.

I bow my head in respect to the original proposer because he stuck
to his guns through this carpet blanking from the peanut gallery,
and the change is in our tree today.  I would have turned my back
and walked away after less than a handful of messages in that
thread.

And that brings me, as I promised earlier, to why I am not subscribed
to -hackers:

I un-subscribed from -hackers several years ago, because I could
not keep up with the email load.  Since then I have dropped off
several other lists as well for the very same reason.

And I still get a lot of email.  A lot of it gets routed to /dev/null
by filters:  People like Brett Glass will never make it onto my
screen, commits to documents in languages I don't understand
likewise, commits to ports as such.  All these things and more go
the winter way without me ever even knowing about it.

But despite these sharp teeth under my mailbox I still get too much
email.

This is where the greener grass comes into the picture:

I wish we could reduce the amount of noise in our lists and I wish
we could let people build a bike shed every so often, and I don't
really care what colour they paint it.

The first of these wishes is about being civil, sensitive and
intelligent in our use of email.

If I could concisely and precisely define a set of criteria for
when one should and when one should not reply to an email so that
everybody would agree and abide by it, I would be a happy man, but
I am too wise to even attempt that.

But let me suggest a few pop-up windows I would like to see
mail-programs implement whenever people send or reply to email
to the lists they want me to subscribe to:

      +------------------------------------------------------------+
      | Your email is about to be sent to several hundred thousand |
      | people, who will have to spend at least 10 seconds reading |
      | it before they can decide if it is interesting.  At least  |
      | two man-weeks will be spent reading your email.  Many of   |
      | the recipients will have to pay to download your email.    |
      |                                                            |
      | Are you absolutely sure that your email is of sufficient   |
      | importance to bother all these people ?                    |
      |                                                            |
      |                  [YES]  [REVISE]  [CANCEL]                 |
      +------------------------------------------------------------+

      +------------------------------------------------------------+
      | Warning:  You have not read all emails in this thread yet. |
      | Somebody else may already have said what you are about to  |
      | say in your reply.  Please read the entire thread before   |
      | replying to any email in it.                               |
      |                                                            |
      |                      [CANCEL]                              |
      +------------------------------------------------------------+

      +------------------------------------------------------------+
      | Warning:  Your mail program have not even shown you the    |
      | entire message yet.  Logically it follows that you cannot  |
      | possibly have read it all and understood it.               |
      |                                                            |
      | It is not polite to reply to an email until you have       |
      | read it all and thought about it.                          |
      |                                                            |
      | A cool off timer for this thread will prevent you from     |
      | replying to any email in this thread for the next one hour |
      |                                                            |
      |                       [Cancel]                             |
      +------------------------------------------------------------+

      +------------------------------------------------------------+
      | You composed this email at a rate of more than N.NN cps    |
      | It is generally not possible to think and type at a rate   |
      | faster than A.AA cps, and therefore you reply is likely to |
      | incoherent, badly thought out and/or emotional.            |
      |                                                            |
      | A cool off timer will prevent you from sending any email   |
      | for the next one hour.                                     |
      |                                                            |
      |                       [Cancel]                             |
      +------------------------------------------------------------+

The second part of my wish is more emotional.  Obviously, the
capacities we had manning the unfriendly fire in the sleep(1)
thread, despite their many years with the project, never cared
enough to do this tiny deed, so why are they suddenly so enflamed
by somebody else so much their junior doing it ?

I wish I knew.

I do know that reasoning will have no power to stop such "reactionaire
conservatism".  It may be that these people are frustrated about
their own lack of tangible contribution lately or it may be a bad
case of "we're old and grumpy, WE know how youth should behave".

Either way it is very unproductive for the project, but I have no
suggestions for how to stop it.  The best I can suggest is to refrain
from fuelling the monsters that lurk in the mailing lists:  Ignore
them, don't answer them, forget they're there.

I hope we can get a stronger and broader base of contributors in
FreeBSD, and I hope we together can prevent the grumpy old men
and the Brett Glasses of the world from chewing them up, spitting
them out and scaring them away before they ever get a leg to the
ground.

For the people who have been lurking out there, scared away from
participating by the gargoyles:  I can only apologise and encourage
you to try anyway, this is not the way I want the environment in
the project to be.

Poul-Henning