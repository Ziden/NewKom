package devsBrsMarotos.mechanic.list.Events;

import java.io.PrintStream;

public enum UpdateType {

    /**
     * 3840000MS 64 MINUTOS
     */
    MIN_64(3840000L),
    /**
     * 1920000MS 32 MINUTOS
     */
    MIN_32(1920000L),
    /**
     * 960000MS 16 MINUTOS
     */
    MIN_16(960000L),
    /**
     * 480000MS 8 MINUTOS
     */
    MIN_08(480000L),
    /**
     * 240000MS 4 MINUTOS
     */
    MIN_04(240000L),
    /**
     * 120000MS 2 MINUTOS
     */
    MIN_02(120000L),
    /**
     * 1 MINUTO 60000MS
     */
    MIN_01(60000L),
    /**
     * 32 SEGUNDOS 32000MS
     */
    SLOWEST(32000L),
    /**
     * 16 SEGUNDOS 16000MS
     */
    SLOWER(16000L),
    /**
     * 4 SEGUNDOS 4000MS
     */
    SLOW(4000L),
    /**
     * 1 SEGUNDO 1000MS
     */
    SEC(1000L),
    /**
     * MEIO SEGUNDO 500MS
     */
    FAST(500L),
    /**
     * 1 QUARTO DE SEGUNDO 250MS
     */
    FASTER(250L),
    /**
     * 1 OITAVO DE SEGUNDO 125MS
     */
    FASTEST(125L),
    /**
     * 1 TICK 49MS
     */
    TICK(49L);

    private long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    private UpdateType(long time) {
        this._time = time;
        this._last = System.currentTimeMillis();
    }

    public boolean Elapsed() {
        if (elapsed(this._last, this._time)) {
            this._last = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public void StartTime() {
        this._timeCount = System.currentTimeMillis();
    }

    public void StopTime() {
        this._timeSpent += System.currentTimeMillis() - this._timeCount;
    }

    public void PrintAndResetTime() {
        System.out.println(name() + " in a second: " + this._timeSpent);
        this._timeSpent = 0L;
    }
}
