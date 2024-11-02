package io.serateam.stewboo.core.services.pomodoro;

public interface PomodoroListener
{
    void onCountdownUpdate(int minutesRemaining, int secondsRemaining);
    void onUpdate(String sessionType);
}
