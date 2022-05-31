package vn.aptech.doccure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.doccure.utils.DateUtils;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicOpeningTimes {

    LocalDateTime opening;
    LocalDateTime closing;

    public boolean isClosed() {
        return this.opening == null && this.closing == null;
    }

    public boolean isOpenNow() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.opening) && now.isBefore(this.closing);
    }

    public String getOpeningText() {
        return DateUtils.toStandardTime(this.opening);
    }

    public String getClosingText() {
        return DateUtils.toStandardTime(this.closing);
    }

    public String getOpeningTime() {
        return this.getOpeningText() + " - " + this.getClosingText();
    }
}
