package com.example.data

import com.example.model.*

/**
 * Sample realistic data for AnsarAcademy.
 * All prices, numbers, and testimonials are clearly marked editable placeholders.
 */
object SampleCourseData {

    val courseInfo = CourseInfo(
        title = "Social Media Growth Mastery",
        tagline = "Social Media Se Apna Career Aur Business Grow Karo 🚀",
        description = "Instagram, Reels, Content Systems aur Client Acquisition seekho practical step-by-step tareeqe se. Zero se shuru karo aur monthly 6-figure creator ya freelancer bano!",
        originalPrice = 4999, // EDITABLE SAMPLE: MRP
        offerPrice = 999,      // EDITABLE SAMPLE: Discounted price
        currencySymbol = "₹",
        discountPercent = 80,
        instructorName = "Rahul Sharma",
        instructorBio = "Top Social Media Strategist & Creator with 500k+ organic followers. Trained 3,000+ creators and consulted 40+ brands across India & Dubai.",
        instructorStats = "500K+ Followers • 8+ Years Experience • 40+ Brand Collaborations",
        totalModules = 10,
        totalLessons = 32,
        totalDurationHours = "24+ Hours",
        rating = 4.9,
        totalStudentsCountSample = "3,420+ Enrolled Students (Sample)"
    )

    val initialModules: List<CourseModule> = listOf(
        CourseModule(
            id = "mod_1",
            number = 1,
            title = "1. Social Media Basics",
            description = "Understand the modern algorithm, audience psychology, and choosing high-paying niches.",
            lessons = listOf(
                CourseLesson(
                    id = "les_1_1",
                    moduleId = "mod_1",
                    title = "How Social Media Algorithms Actually Work in 2026",
                    durationMinutes = 24,
                    summary = "Learn watch time, save-to-share ratios, retention curves, and what signals triggers algorithmic distribution.",
                    isCompleted = true,
                    keyTakeaways = listOf(
                        "Watch time is king: Focus on the first 3 seconds and completion rate",
                        "Shares > Likes: Algorithms prioritize content that creates conversations",
                        "Niche clarity: Don't confuse the algorithm with mixed topics"
                    ),
                    resources = listOf("Algorithm Checklist.pdf", "Niche Selection Matrix.xlsx")
                ),
                CourseLesson(
                    id = "les_1_2",
                    moduleId = "mod_1",
                    title = "Choosing Your Profitable Niche (Creator vs Business)",
                    durationMinutes = 30,
                    summary = "Step-by-step formula to pick a niche that aligns with your skills and has monetization potential.",
                    isCompleted = true,
                    keyTakeaways = listOf(
                        "The 3 Evergreen Wealth Niches: Health, Wealth, Relationships",
                        "Micro-niching strategy for faster initial authority",
                        "Evaluating advertiser and brand budget in your topic"
                    )
                ),
                CourseLesson(
                    id = "les_1_3",
                    moduleId = "mod_1",
                    title = "Platform Ecosystem: Instagram vs YouTube vs LinkedIn",
                    durationMinutes = 20,
                    summary = "How to syndicate content across platforms without burning out.",
                    isCompleted = false,
                    keyTakeaways = listOf(
                        "Repurposing 1 long-form idea into 4 short reels",
                        "Platform-specific context and formatting nuances"
                    )
                )
            )
        ),
        CourseModule(
            id = "mod_2",
            number = 2,
            title = "2. Instagram Profile Optimization",
            description = "Convert profile visitors into followers with high-converting bios, highlights, and link hubs.",
            lessons = listOf(
                CourseLesson(
                    id = "les_2_1",
                    moduleId = "mod_2",
                    title = "The 5-Second Bio Formula That Drives 40% Follow Conversion",
                    durationMinutes = 22,
                    summary = "Craft a magnetic headline, who you help, what result you deliver, and an irresistible lead magnet CTA.",
                    isCompleted = false,
                    keyTakeaways = listOf(
                        "Clarity over cleverness: Avoid vague poetic bios",
                        "SEO keyword placement in Name field vs Username",
                        "Single clear CTA above the fold"
                    )
                ),
                CourseLesson(
                    id = "les_2_2",
                    moduleId = "mod_2",
                    title = "Designing High-Trust Highlight Funnels",
                    durationMinutes = 28,
                    summary = "Build the 4 essential highlights: About, Proof/Results, Offer/Services, and Free Value.",
                    isCompleted = false,
                    keyTakeaways = listOf(
                        "Highlight covers consistency and color psychology",
                        "Interactive poll stories saved as ongoing social proof"
                    )
                ),
                CourseLesson(
                    id = "les_2_3",
                    moduleId = "mod_2",
                    title = "Link-in-Bio Setup & Lead Magnet Integration",
                    durationMinutes = 18,
                    summary = "Capturing WhatsApp and Email leads directly from your profile visitors.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_3",
            number = 3,
            title = "3. Reels Strategy",
            description = "Mastering 9:16 short form storytelling, camera confidence, and retention pacing.",
            lessons = listOf(
                CourseLesson(
                    id = "les_3_1",
                    moduleId = "mod_3",
                    title = "The Anatomy of a 100K+ View Reel",
                    durationMinutes = 35,
                    summary = "Breaking down hook, pattern interrupt, fast body delivery, value punch, and looping outro.",
                    isCompleted = false,
                    keyTakeaways = listOf(
                        "Keep total duration between 18 to 34 seconds for highest completion percentage",
                        "Change camera angles or visual overlays every 2.5 seconds",
                        "Use seamless loops for bonus repeat views"
                    )
                ),
                CourseLesson(
                    id = "les_3_2",
                    moduleId = "mod_3",
                    title = "Camera Confidence & Smartphone Lighting Hacks",
                    durationMinutes = 26,
                    summary = "Professional video output on budget Android smartphones using natural window light & affordable mics.",
                    isCompleted = false
                ),
                CourseLesson(
                    id = "les_3_3",
                    moduleId = "mod_3",
                    title = "Audio Selection Strategy (Trending vs Original Sound)",
                    durationMinutes = 19,
                    summary = "When to ride trending audio waves versus when to establish your authoritative voice.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_4",
            number = 4,
            title = "4. Content Creation",
            description = "Fast batch-production systems: scriptwriting, CapCut editing, sound design, and motion graphics.",
            lessons = listOf(
                CourseLesson(
                    id = "les_4_1",
                    moduleId = "mod_4",
                    title = "Batch Shooting 10 Reels in Under 2 Hours",
                    durationMinutes = 40,
                    summary = "Our agency workflow for planning, staging, shooting, and labeling video files in one sitting.",
                    isCompleted = false,
                    keyTakeaways = listOf(
                        "Script teleprompter setup on mobile",
                        "Changing 3 jackets/shirts to shoot multi-day content in 1 afternoon"
                    )
                ),
                CourseLesson(
                    id = "les_4_2",
                    moduleId = "mod_4",
                    title = "CapCut Masterclass: Dynamic Captions, Zooms & SFX",
                    durationMinutes = 45,
                    summary = "Full screen recording walkthrough of editing a viral reel with dynamic animated words and whoosh sound effects.",
                    isCompleted = false
                ),
                CourseLesson(
                    id = "les_4_3",
                    moduleId = "mod_4",
                    title = "Carousel & Graphic Design in Canva for Non-Designers",
                    durationMinutes = 30,
                    summary = "Creating 10-slide educational carousels that drive thousands of saves and reposts.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_5",
            number = 5,
            title = "5. Viral Hooks",
            description = "The first 3 seconds: psychology of visual, verbal, and text pattern interrupts.",
            lessons = listOf(
                CourseLesson(
                    id = "les_5_1",
                    moduleId = "mod_5",
                    title = "The 50 Proven Viral Hook Formulas (Hindi & English)",
                    durationMinutes = 32,
                    summary = "Controversial statements, counter-intuitive truths, negative framing, and FOMO hooks.",
                    isCompleted = false,
                    keyTakeaways = listOf(
                        "Negative hooks perform 60% better: 'Stop doing this' vs 'Do this'",
                        "Curiosity gap: Tease the result before revealing the method",
                        "Action-oriented text on thumbnail"
                    )
                ),
                CourseLesson(
                    id = "les_5_2",
                    moduleId = "mod_5",
                    title = "Visual Pattern Interrupts (Props, Movement & Cuts)",
                    durationMinutes = 25,
                    summary = "Walking into frame, pouring coffee, holding unexpected items to stop instant thumb scrolling.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_6",
            number = 6,
            title = "6. Caption & Hashtag Strategy",
            description = "Search Engine Optimization (SEO) for Instagram, discoverability, and keyword tagging.",
            lessons = listOf(
                CourseLesson(
                    id = "les_6_1",
                    moduleId = "mod_6",
                    title = "Instagram SEO: Ranking on the Explore & Search Bar",
                    durationMinutes = 27,
                    summary = "How Instagram indexes your audio transcript, visual OCR, and caption text for search queries.",
                    isCompleted = false
                ),
                CourseLesson(
                    id = "les_6_2",
                    moduleId = "mod_6",
                    title = "The 3x3 Hashtag Method vs Niche Keyword Stacking",
                    durationMinutes = 21,
                    summary = "Stop using 30 spam hashtags. Learn modern 5-8 targeted semantic tags that target actual buyers.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_7",
            number = 7,
            title = "7. Content Calendar",
            description = "Never run out of ideas. Build an automated Notion / Google Sheets 30-day content engine.",
            lessons = listOf(
                CourseLesson(
                    id = "les_7_1",
                    moduleId = "mod_7",
                    title = "The 4 Content Pillars: Educate, Entertain, Inspire, Sell",
                    durationMinutes = 34,
                    summary = "Balancing viral reach posts with conversion posts so you grow followers AND bank balance simultaneously.",
                    isCompleted = false
                ),
                CourseLesson(
                    id = "les_7_2",
                    moduleId = "mod_7",
                    title = "Setting Up Your 30-Day Automated Calendar",
                    durationMinutes = 29,
                    summary = "Walkthrough of our ready-made Notion template with content status tags and asset links.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_8",
            number = 8,
            title = "8. Personal Branding",
            description = "Building authority, trust, and a dedicated tribe that buys whatever you recommend.",
            lessons = listOf(
                CourseLesson(
                    id = "les_8_1",
                    moduleId = "mod_8",
                    title = "Defining Your Unique Mechanism & Signature Story",
                    durationMinutes = 36,
                    summary = "Why people follow personalities, not just generic tips. Share your struggles, wins, and philosophy.",
                    isCompleted = false
                ),
                CourseLesson(
                    id = "les_8_2",
                    moduleId = "mod_8",
                    title = "Story Selling: Turning Instagram Stories into Cash Flow",
                    durationMinutes = 31,
                    summary = "Daily 5-story sequence for relationship building and natural direct message conversations.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_9",
            number = 9,
            title = "9. Monetization",
            description = "Affiliate marketing, digital products, brand sponsorships, and paid community memberships.",
            lessons = listOf(
                CourseLesson(
                    id = "les_9_1",
                    moduleId = "mod_9",
                    title = "The 5 Revenue Streams for Creators with Under 10K Followers",
                    durationMinutes = 38,
                    summary = "You don't need 100k followers to earn. How to make ₹30k - ₹75k with a micro audience of 2k true fans.",
                    isCompleted = false
                ),
                CourseLesson(
                    id = "les_9_2",
                    moduleId = "mod_9",
                    title = "Pricing Your Brand Collaborations & Media Kit Blueprint",
                    durationMinutes = 33,
                    summary = "Calculate your CPM rates, negotiate usage rights, and create a 1-page PDF media kit that brands love.",
                    isCompleted = false
                )
            )
        ),
        CourseModule(
            id = "mod_10",
            number = 10,
            title = "10. Client Acquisition",
            description = "Landing high-ticket international and domestic freelance social media clients paying ₹25k - ₹60k/month.",
            lessons = listOf(
                CourseLesson(
                    id = "les_10_1",
                    moduleId = "mod_10",
                    title = "Cold DM & Email Outreach Scripts That Get 35% Reply Rate",
                    durationMinutes = 42,
                    summary = "Exact word-for-word pitch scripts for Instagram DMs, LinkedIn, and cold email without being pushy.",
                    isCompleted = false
                ),
                CourseLesson(
                    id = "les_10_2",
                    moduleId = "mod_10",
                    title = "Closing the Discovery Call & Sending Professional Retainer Contracts",
                    durationMinutes = 36,
                    summary = "How to handle objections like 'Aapka charge zyada hai' and secure 3-month upfront retainers.",
                    isCompleted = false
                )
            )
        )
    )

    // Clearly marked sample editable testimonials
    val sampleTestimonials: List<Testimonial> = listOf(
        Testimonial(
            id = "test_1",
            studentName = "Aman Verma",
            handle = "@amanv_fitness",
            avatarEmoji = "🏋️",
            role = "Fitness Coach (Sample)",
            rating = 5,
            commentHinglish = "Maine yeh course 2 mahine pehle liya tha. Pehle mere reels par 300 views aate the, Module 3 aur 5 apply karne ke baad mere 3 reels 200k+ cross hue aur 8 high-ticket personal training clients mile! Paisa vasool!",
            resultBadge = "+18.4K Followers in 60 Days",
            isSampleEditable = true
        ),
        Testimonial(
            id = "test_2",
            studentName = "Priya Sharma",
            handle = "@priyadesigns.in",
            avatarEmoji = "🎨",
            role = "Freelance Graphic Designer (Sample)",
            rating = 5,
            commentHinglish = "Best course for beginners! Rahul sir ne client acquisition module mein jo cold DM scripts di thi, usi se mujhe ₹45,000/month ka retainer client mila. AI Assistant tools feature bhi daily content banane mein bohot help karta hai.",
            resultBadge = "₹45K/Month Retainer Closed",
            isSampleEditable = true
        ),
        Testimonial(
            id = "test_3",
            studentName = "Kunal Mehta",
            handle = "@kunal.techhindi",
            avatarEmoji = "📱",
            role = "Tech Creator & Student (Sample)",
            rating = 5,
            commentHinglish = "Mujhe camera ke saamne aane me darr lagta tha. But lighting aur hook formulas ne mera game change kar diya. Aaj mera account monetize ho chuka hai aur brand deals bhi mil rahi hain.",
            resultBadge = "1.2k to 42k Organic Growth",
            isSampleEditable = true
        ),
        Testimonial(
            id = "test_4",
            studentName = "Sneha Patel",
            handle = "@bakerybysneha",
            avatarEmoji = "🧁",
            role = "Home Baker & Business Owner (Sample)",
            rating = 5,
            commentHinglish = "Mere local business ke orders 3x badh gaye jab maine reels par local SEO aur viral hooks implement kiye. Ab mujhe paid ads par paise barbad nahi karne padte!",
            resultBadge = "3X Daily Orders from Instagram",
            isSampleEditable = true
        )
    )

    val faqs: List<FaqItem> = listOf(
        FaqItem(
            id = "faq_1",
            questionHinglish = "Kya yeh course beginners ke liye suitable hai jinko zero social media knowledge hai?",
            answerHinglish = "Haan, bilkul! Course bilkul basic level se shuru hota hai jahan algorithm, niche selection aur account setup sikhaya jaata hai, aur step-by-step advanced monetization tak le jaya jata hai."
        ),
        FaqItem(
            id = "faq_2",
            questionHinglish = "Kya course lene ke liye expensive camera ya laptop chahiye?",
            answerHinglish = "Nahi! 95% content creation aap apne normal smartphone aur free mobile apps (jaise CapCut, Canva) se kar sakte hain. Module 3 & 4 mein smartphone se hi cinematic output nikalna sikhaya gaya hai."
        ),
        FaqItem(
            id = "faq_3",
            questionHinglish = "Course access kitne time tak rahega?",
            answerHinglish = "Aapko Lifetime Access milta hai. Sath hi future mein aane wale saare new updates aur bonus modules bhi bilkul FREE mein aapke dashboard par add hote rahenge."
        ),
        FaqItem(
            id = "faq_4",
            questionHinglish = "Kya mujhe course completion certificate milega?",
            answerHinglish = "Haan! Jab aap saare 10 modules complete kar lenge, aapke student dashboard par 'AnsarAcademy Certified Social Media Strategist' digital certificate generate ho jayega jise aap LinkedIn ya resume par add kar sakte hain."
        ),
        FaqItem(
            id = "faq_5",
            questionHinglish = "Refund policy kya hai?",
            answerHinglish = "Aapko 7-Day 100% No-Questions-Asked Money-Back Guarantee milti hai. Agar aapko content helpful nahi lagta, bas support team ko message karein aur aapka full refund process kar diya jayega."
        ),
        FaqItem(
            id = "faq_6",
            questionHinglish = "Doubt clearing aur support kaise milega?",
            answerHinglish = "Aapko hamara private Student VIP Community access milta hai jahan weekly live Q&A sessions hote hain, plus app ke andar hi AI Social Media Assistant 24/7 available hai aapke content ideas generate karne ke liye."
        )
    )

    val studentBenefits: List<StudentBenefit> = listOf(
        StudentBenefit(
            id = "ben_1",
            title = "Lifetime Course Access",
            description = "Sabhi 10 modules aur 30+ lessons ka lifetime access bina kisi monthly fee ke.",
            iconEmoji = "♾️"
        ),
        StudentBenefit(
            id = "ben_2",
            title = "Built-in AI Social Media Assistant",
            description = "Gemini AI dwara 1-click Reel ideas, captions, hooks aur 30-day calendar generator.",
            iconEmoji = "🤖"
        ),
        StudentBenefit(
            id = "ben_3",
            title = "VIP Community & Weekly Q&A",
            description = "Like-minded creators aur freelancers ke sath network karein aur doubts clear karein.",
            iconEmoji = "👥"
        ),
        StudentBenefit(
            id = "ben_4",
            title = "50+ Ready-to-Use Templates",
            description = "Canva templates, cold pitch scripts, proposal agreements, aur Notion content tracker.",
            iconEmoji = "📂"
        ),
        StudentBenefit(
            id = "ben_5",
            title = "Industry Recognized Certificate",
            description = "Course completion certificate jo aapko freelance clients se trust dilane me madad karega.",
            iconEmoji = "🎓"
        ),
        StudentBenefit(
            id = "ben_6",
            title = "7-Day 100% Money-Back Guarantee",
            description = "Risk-free learning! Pasand na aane par 7 din ke andar 100% refund.",
            iconEmoji = "🛡️"
        )
    )

    val downloadableResources: List<DownloadableResource> = listOf(
        DownloadableResource(
            id = "res_1",
            title = "100+ Viral Hook Swipe File (Hindi & English)",
            category = "Cheat Sheet PDF",
            fileSize = "4.2 MB",
            description = "Tested opening lines across fitness, finance, tech, business and fashion niches."
        ),
        DownloadableResource(
            id = "res_2",
            title = "30-Day Content Calendar & Notion Workflow",
            category = "Notion / Excel Template",
            fileSize = "1.8 MB",
            description = "Automated tracker for scriptwriting, shooting dates, and analytics metrics."
        ),
        DownloadableResource(
            id = "res_3",
            title = "Freelance Social Media Pitch Deck & Contract Agreement",
            category = "Word / PDF Contract",
            fileSize = "2.5 MB",
            description = "Legally vetted retainer contract and scope of work agreement for agency clients."
        ),
        DownloadableResource(
            id = "res_4",
            title = "25 Premium Canva Carousel & Story Templates",
            category = "Canva Direct Links",
            fileSize = "Cloud Link",
            description = "High-converting aesthetic designs ready to edit with your brand fonts and colors."
        ),
        DownloadableResource(
            id = "res_5",
            title = "Instagram Growth & Reel Audio Tracker",
            category = "Spreadsheet",
            fileSize = "1.1 MB",
            description = "Weekly analytics sheet to measure retention rate, follow velocity, and reach."
        )
    )

    val sampleEnrolledStudents: List<EnrollmentRecord> = listOf(
        EnrollmentRecord(
            orderId = "SB-ORD-9821",
            studentName = "Aman Verma",
            studentEmail = "aman.v@example.com",
            studentPhone = "+91 98765 43210",
            amount = 999,
            paymentMethod = "UPI (Google Pay)",
            status = "ACTIVE_ENROLLED",
            timestamp = System.currentTimeMillis() - 86400000 * 3
        ),
        EnrollmentRecord(
            orderId = "SB-ORD-9822",
            studentName = "Priya Sharma",
            studentEmail = "priya.s@example.com",
            studentPhone = "+91 98111 22334",
            amount = 999,
            paymentMethod = "PhonePe UPI",
            status = "ACTIVE_ENROLLED",
            timestamp = System.currentTimeMillis() - 86400000 * 2
        ),
        EnrollmentRecord(
            orderId = "SB-ORD-9823",
            studentName = "Kunal Mehta",
            studentEmail = "kunal.m@example.com",
            studentPhone = "+91 97222 33445",
            amount = 999,
            paymentMethod = "Credit Card (HDFC)",
            status = "ACTIVE_ENROLLED",
            timestamp = System.currentTimeMillis() - 86400000 * 1
        ),
        EnrollmentRecord(
            orderId = "SB-ORD-9824",
            studentName = "Rohan Das",
            studentEmail = "rohan.das@example.com",
            studentPhone = "+91 99000 88776",
            amount = 999,
            paymentMethod = "Paytm UPI",
            status = "ACTIVE_ENROLLED",
            timestamp = System.currentTimeMillis() - 3600000 * 4
        )
    )
}
