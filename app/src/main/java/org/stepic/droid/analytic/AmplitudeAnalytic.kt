package org.stepic.droid.analytic

interface AmplitudeAnalytic {
    object Properties {
        const val STEPIK_ID = "stepik_id"
        const val SUBMISSIONS_COUNT = "submissions_count"
        const val COURSES_COUNT = "courses_count"
        const val SCREEN_ORIENTATION = "screen_orientation"
        const val APPLICATION_ID = "application_id"
        const val PUSH_PERMISSION = "push_permission"
        const val STREAKS_NOTIFICATIONS_ENABLED = "streaks_notifications_enabled"
        const val TEACHING_COURSES_COUNT = "teaching_courses_count"
        const val IS_NIGHT_MODE_ENABLED = "is_night_mode_enabled"
        const val IS_AR_SUPPORTED = "is_ar_supported"
    }

    object Launch {
        const val FIRST_TIME = "Launch first time"
        const val SESSION_START = "Session start"
    }

    object Branch {
        const val LINK_OPENED = "Branch Link Opened"

        const val PARAM_CAMPAIGN = "campaign"
        const val IS_FIRST_SESSION = "is_first_session"
    }

    object Onboarding {
        const val SCREEN_OPENED = "Onboarding screen opened"
        const val CLOSED = "Onboarding closed"
        const val COMPLETED = "Onboarding completed"

        const val PARAM_SCREEN = "screen"
    }

    object Auth {
        const val LOGGED_ID = "Logged in"
        const val REGISTERED = "Registered"

        const val PARAM_SOURCE = "source"
        const val VALUE_SOURCE_EMAIL = "email"
    }

    object Course {
        const val JOINED = "Course joined"
        const val UNSUBSCRIBED = "Course unsubscribed"
        const val CONTINUE_PRESSED = "Continue course pressed"
        const val BUY_COURSE_PRESSED = "Buy course pressed"

        object Params {
            const val COURSE = "course"
            const val SOURCE = "source"
        }

        object Values {
            const val WIDGET = "widget"
            const val PREVIEW = "preview"

            const val COURSE_WIDGET = "course_widget"
            const val HOME_WIDGET = "home_widget"
            const val COURSE_SCREEN = "course_screen"
        }
    }

    object CoursePreview {
        const val COURSE_PREVIEW_SCREEN_OPENED = "Course preview screen opened"

        object Params {
            const val COURSE = "course"
            const val TITLE = "title"
            const val IS_PAID = "is_paid"
        }
    }

    object Steps {
        const val SUBMISSION_MADE = "Submission made"
        const val STEP_OPENED = "Step opened"

        const val STEP_EDIT_OPENED = "Step edit opened"
        const val STEP_EDIT_COMPLETED = "Step edit completed"

        const val STEP_SOLUTIONS_OPENED = "Step solutions opened"

        object Params {
            const val SUBMISSION = "submission"
            const val TYPE = "type"
            const val LANGUAGE = "language"
            const val NUMBER = "number"
            const val STEP = "step"
            const val LOCAL = "local"
            const val IS_ADAPTIVE = "is_adaptive"
        }
    }

    object Downloads {
        const val STARTED = "Download started"
        const val CANCELLED = "Download cancelled"
        const val DELETED = "Download deleted"
        const val SCREEN_OPENED = "Downloads screen opened"
        const val DELETE_CONFIRMATION_INTERACTED = "Delete downloads confirmation interacted"

        const val PARAM_CONTENT = "content"
        const val PARAM_SOURCE = "source"
        const val PARAM_RESULT = "result"

        object Values {
            /**
             *  Content
             */
            const val COURSE = "course"
            const val SECTION = "section"
            const val LESSON = "lesson"

            /**
             *  Source
             */
            const val SYLLABUS = "syllabus"
            const val DOWNLOADS = "downloads"
            /**
             *  Result
             */
            const val YES = "yes"
            const val NO = "no"
        }
    }

    object Search {
        const val COURSE_SEARCH_CLICKED = "Course search clicked"
        const val SEARCHED = "Course searched"

        const val PARAM_SUGGESTION = "suggestion"
    }

    object Stories {
        const val STORY_OPENED = "Story opened"
        const val STORY_PART_OPENED = "Story part opened"
        const val BUTTON_PRESSED = "Story button pressed"
        const val STORY_CLOSED = "Story closed"

        object Values {
            const val STORY_ID = "id"
            const val POSITION = "position"
            const val CLOSE_TYPE = "type"

            object CloseTypes {
                const val AUTO = "automatic"
                const val SWIPE = "swipe"
                const val CROSS = "cross"
            }
        }
    }

    object Video {
        const val PLAY_IN_BACKGROUND = "Video played in background"
        const val PLAYBACK_SPEED_CHANGED = "Video rate changed"
        const val AUTOPLAY = "Video autoplay changed"

        object Params {
            const val SOURCE = "source"
            const val TARGET = "target"

            const val IS_ENABLED = "is_enabled"
        }
    }

    object Deadlines {
        const val PERSONAL_DEADLINE_CREATED = "Personal deadline created"
        const val SCHEDULE_PRESSED = "Personal deadline schedule button pressed"

        object Params {
            const val HOURS = "hours"
        }
    }

    object Achievements {
        const val NOTIFICATION_RECEIVED = "Achievement notification received"

        object Params {
            const val KIND = "achievement_kind"
            const val LEVEL = "achievement_level"
        }
    }

    object ProfileEdit {
        const val SCREEN_OPENED = "Profile edit screen opened"
        const val SAVED = "Profile edit saved"
    }

    object Adaptive {
        const val RATING_OPENED = "Adaptive rating opened"

        object Params {
            const val COURSE = "course"
        }
    }

    object CourseReview {
        const val SCREEN_OPENED = "Course reviews screen opened"

        const val REVIEW_CREATED = "Course review created"
        const val REVIEW_UPDATED = "Course review updated"
        const val REVIEW_REMOVED = "Course review deleted"

        object Params {
            const val COURSE = "course"
            const val RATING = "rating"
            const val FROM_RATING = "from_rating"
            const val TO_RATING = "to_rating"
        }
    }

    object FontSize {
        const val FONT_SIZE_SELECTED = "Font size selected"

        object Params {
            const val SIZE = "size"
        }
    }

    object Home {
        const val HOME_SCREEN_OPENED = "Home screen opened"
    }

    object Catalog {
        const val CATALOG_SCREEN_OPENED = "Catalog screen opened"
    }

    object Profile {
        const val PROFILE_SCREEN_OPENED = "Profile screen opened"
        const val PROFILE_STAT_CLICKED = "Profile stat clicked"

        object Params {
            const val STATE = "state"
            const val TYPE = "type"
            const val ID = "id"
        }

        object Values {
            /**
             * State
             */
            const val SELF = "self"
            const val OTHER = "other"

            /**
             * Type
             */
            const val REPUTATION = "reputation"
            const val KNOWLEDGE = "knowledge"
        }
    }

    object Notifications {
        const val NOTIFICATION_SCREEN_OPENED = "Notifications screen opened"
    }

    object LocalSubmissions {
        const val LOCAL_SUBMISSIONS_SCREEN_OPENED = "Local submissions screen opened"
        const val LOCAL_SUBMISSION_ITEM_CLICKED = "Local submission item clicked"
        const val LOCAL_SUBMISSION_MADE = "Local submission made"

        object Params {
            /**
             *  Submission item clicked
             */
            const val STEP_ID = "step_id"

            /**
             *  Local submission made
             */
            const val TYPE = "type"
            const val LANGUAGE = "language"
            const val NUMBER = "number"
            const val STEP = "step"
        }
    }

    object RunCode {
        const val RUN_CODE_LAUNCHED = "Run code launched"

        object Params {
            const val STEP_ID = "step_id"
        }
    }
}