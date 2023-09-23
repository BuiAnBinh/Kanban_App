package theWholeThing

import java.time.{Duration, LocalDate}
import java.time.temporal.ChronoUnit

class TimeTracker(val deadline: LocalDate) {
  private def timeNow = LocalDate.now()
  private def durationToDeadline = ChronoUnit.DAYS.between(this.timeNow, deadline)
  def dayToDeadline = durationToDeadline
}
