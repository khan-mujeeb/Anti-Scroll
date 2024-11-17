package com.example.antiscroll.data

import kotlin.random.Random

object TimeQuotes {
    val quotes = listOf(
        Quote(
            "Time is the wisest of all things that are; for it brings everything to light.",
            "Thales of Miletus"
        ),
        Quote(
            "The present time has one advantage over every other—it is our own.",
            "Charles Caleb Colton"
        ),
        Quote(
            "Time is a created thing. To say 'I don't have time' is like saying, 'I don't want to.'",
            "Lao Tzu"
        ),
        Quote("Lost time is never found again.", "Benjamin Franklin"),
        Quote("Time flies over us, but leaves its shadow behind.", "Nathaniel Hawthorne"),
        Quote("Time and tide wait for no man.", "Geoffrey Chaucer"),
        Quote(
            "All we have to decide is what to do with the time that is given us.",
            "J.R.R. Tolkien"
        ),
        Quote(
            "The only reason for time is so that everything doesn’t happen at once.",
            "Albert Einstein"
        ),
        Quote("Time is the moving image of eternity.", "Plato"),
        Quote("There is nothing more precious than time.", "Jean de La Bruyère"),
        Quote("Time is the most valuable thing a man can spend.", "Theophrastus"),
        Quote("The two most powerful warriors are patience and time.", "Leo Tolstoy"),
        Quote("You may delay, but time will not.", "Benjamin Franklin"),
        Quote("Time is what we want most, but what we use worst.", "William Penn"),
        Quote(
            "Time changes everything except something within us which is always surprised by change.",
            "Thomas Hardy"
        ),
        Quote("We must use time as a tool, not as a couch.", "John F. Kennedy"),
        Quote("Time is what keeps everything from happening at once.", "Ray Cummings"),
        Quote("Time is a great healer, but a poor beautician.", "Lucille S. Harper"),
        Quote(
            "The future is something which everyone reaches at the rate of sixty minutes an hour, whatever he does, whoever he is.",
            "C.S. Lewis"
        ),
        Quote("Do not squander time, for that’s the stuff life is made of.", "Benjamin Franklin"),
        Quote(
            "A man who dares to waste one hour of time has not discovered the value of life.",
            "Charles Darwin"
        ),
        Quote("Time is the longest distance between two places.", "Tennessee Williams"),
        Quote("Time is the fire in which we burn.", "Delmore Schwartz"),
        Quote("Time is a companion that reminds us to cherish each moment.", "Jean-Luc Picard"),
        Quote(
            "In reality, killing time is only the name for another of the multifarious ways by which Time kills us.",
            "Osbert Sitwell"
        ),
        Quote("Time flies like an arrow; fruit flies like a banana.", "Groucho Marx"),
        Quote(
            "Time has no independent existence apart from the order of events by which we measure it.",
            "Albert Einstein"
        ),
        Quote("Time ripens all things; no man is born wise.", "Miguel de Cervantes"),
        Quote(
            "Time, which changes people, does not alter the image we have retained of them.",
            "Marcel Proust"
        ),
        Quote("Better three hours too soon than a minute too late.", "William Shakespeare"),
        Quote("Time is an illusion.", "Albert Einstein"),
        Quote(
            "There is no greater sorrow than to recall happiness in times of misery.",
            "Dante Alighieri"
        ),
        Quote("There are moments and then there are lifetimes.", "Terry Pratchett"),
        Quote("You can’t turn back the clock. But you can wind it up again.", "Bonnie Prudden"),
        Quote("Time, as it grows old, teaches all things.", "Aeschylus"),
        Quote("Time goes, you say? Ah no! Alas, Time stays, we go.", "Henry Austin Dobson"),
        Quote(
            "Time is the coin of your life. It is the only coin you have, and only you can determine how it will be spent.",
            "Carl Sandburg"
        ),
        Quote(
            "The more sand has escaped from the hourglass of our life, the clearer we should see through it.",
            "Jean-Paul Sartre"
        ),
        Quote("Time discovers truth.", "Seneca"),
        Quote("Men talk of killing time, while time quietly kills them.", "Dion Boucicault"),
        Quote("To realize the unimportance of time is the gate to wisdom.", "Bertrand Russell"),
        Quote(
            "They always say time changes things, but you actually have to change them yourself.",
            "Andy Warhol"
        ),
        Quote(
            "Time passes unhindered. When we make mistakes, we cannot turn the clock back and try again. All we can do is use the present well.",
            "Dalai Lama"
        ),
        Quote("Time brings all things to pass.", "Aeschylus"),
        Quote("Nothing endures but change.", "Heraclitus"),
        Quote(
            "Time is not measured by the passing of years but by what one does, what one feels, and what one achieves.",
            "Jawaharlal Nehru"
        ),
        Quote(
            "Time is like a handful of sand—the tighter you grasp it, the faster it runs through your fingers.",
            "Henry David Thoreau"
        ),
        Quote("Time stays long enough for anyone who will use it.", "Leonardo da Vinci"),
        Quote("One must work with time and not against it.", "Ursula K. Le Guin"),
        Quote(
            "The bad news is time flies. The good news is you’re the pilot.",
            "Michael Altshuler"
        ),
        Quote("Time is what we make of it.", "Napoleon Bonaparte"),
        Quote("The way we spend our time defines who we are.", "Jonathan Estrin"),
        Quote("Time is what we make of it, not what it makes of us.", "James Allen"),
        Quote(
            "The past always looks better than it was. It’s only pleasant because it isn’t here.",
            "Finley Peter Dunne"
        ),
        Quote("Yesterday is but today’s memory, and tomorrow is today’s dream.", "Khalil Gibran"),
        Quote("There is more to life than increasing its speed.", "Mahatma Gandhi"),
        Quote("If you judge people, you have no time to love them.", "Mother Teresa"),
        Quote("Time is the substance from which I am made.", "Jorge Luis Borges"),
        Quote("Time is the justice that examines all offenders.", "William Shakespeare"),
        Quote(
            "Time makes fools of us all. Our only comfort is that greater shall come after us.",
            "E.T. Bell"
        ),
        Quote("Time flies, but you’re the pilot.", "Michael Altshuler"),
        Quote("Time wounds all heels.", "Jane Ace"),
        Quote("Time is the most valuable thing a man can spend.", "Theophrastus"),
        Quote("Time is not a thief, unless you allow it to steal your life.", "Dodinsky"),
        Quote(
            "An inch of time is an inch of gold, but you can’t buy that inch of time with an inch of gold.",
            "Chinese Proverb"
        ),
        Quote(
            "Yesterday is gone. Tomorrow has not yet come. We have only today. Let us begin.",
            "Mother Teresa"
        ),
        Quote("Don’t wait. The time will never be just right.", "Napoleon Hill"),
        Quote(
            "Take care of the minutes, for the hours will take care of themselves.",
            "Lord Chesterfield"
        ),
        Quote("The time is always right to do what is right.", "Martin Luther King Jr."),
        Quote(
            "Time is like money; the less we have of it to spare, the further we make it go.",
            "Josh Billings"
        ),
        Quote("Your time is limited, so don’t waste it living someone else’s life.", "Steve Jobs"),
        Quote("Time itself comes in drops.", "William James"),
        Quote("Time does not change us. It just unfolds us.", "Max Frisch"),
        Quote("He who knows most grieves most for wasted time.", "Dante Alighieri"),
        Quote(
            "You can’t stop the future. You can’t rewind the past. The only way to learn the secret…is to press play.",
            "Jay Asher"
        ),
        Quote("Time is but the stream I go a-fishing in.", "Henry David Thoreau"),
        Quote("We live in a world of time, yet long for eternity.", "Sunday Adelaja"),
        Quote(
            "Time is a resource. If you have the right skills, you can always make more money, but you can never make more time.",
            "Tony Robbins"
        ),
        Quote("The more one judges, the less one loves.", "Honore de Balzac"),
        Quote(
            "People who cannot find time for recreation are obliged sooner or later to find time for illness.",
            "John Wanamaker"
        ),
        Quote("Time has a way of showing us what really matters.", "Margaret Peters"),
        Quote("The future depends on what we do in the present.", "Mahatma Gandhi"),
        Quote(
            "Life is not measured by the number of breaths we take, but by the moments that take our breath away.",
            "Anonymous"
        ),
        Quote("Time flies. It's up to you to be the navigator.", "Robert Orben"),
        Quote(
            "Life is short. Live it. Love is rare. Grab it. Anger is bad. Let it go. Fear is a mind-killer. Face it. Memories are sweet. Cherish them.",
            "Anonymous"
        ),
        Quote("Take time for all things: great haste makes great waste.", "Benjamin Franklin"),
        Quote(
            "Time is the friend of the wonderful company, the enemy of the mediocre.",
            "Warren Buffett"
        ),
        Quote("Time is the wisest counselor of all.", "Pericles"),
        Quote(
            "Time flies, memories fade, feelings change, people leave, but hearts never forget.",
            "Anonymous"
        )
    )

    fun getQuote(): Quote {
        val random = java.util.Random()
        return quotes[random.nextInt(quotes.size)]
    }

}