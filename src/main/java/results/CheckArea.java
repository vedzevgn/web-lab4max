package results;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "results")
@ApplicationScoped
public class CheckArea implements Serializable {
    private long id;
    private String user_hash;
    private double x;
    private double y;
    private double r;
    private boolean result;
    private LocalDateTime executedAt;
    private long execTime;

    public CheckArea() {
        super();
    }

    @Column(name = "x")
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    @Column(name = "y")
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    @Column(name = "r")
    public double getR() {
        return r;
    }
    public void setR(double r) {
        this.r = r;
    }

    @Column(name = "result")
    public boolean isResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }

    @Column(name = "executed_at")
    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    @Column(name = "exec_time")
    public long getExecTime() {
        return execTime;
    }
    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    @Column(name = "user_hash")
    public String getUser_hash() {
        return user_hash;
    }
    public void setUser_hash(String userHash) {
        this.user_hash = userHash;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckArea)) return false;
        CheckArea bean = (CheckArea) o;
        return getId() == bean.getId() && Double.compare(getX(), bean.getX()) == 0 && Double.compare(getY(), bean.getY()) == 0 && Double.compare(getR(), bean.getR()) == 0 && isResult() == bean.isResult() && getExecTime() == bean.getExecTime() && Objects.equals(getExecutedAt(), bean.getExecutedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getX(), getY(), getR(), isResult(), getExecutedAt(), getExecTime());
    }

    @Override
    public String toString() {
        return "CheckAreaBean{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", result=" + result +
                ", executedAt=" + executedAt +
                ", execTime=" + execTime +
                '}';
    }
}