package com.hospital.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class SlotGenerator {

    public List<LocalDateTime> generateSlots() {
        List<LocalDateTime> slots = new ArrayList<>();
        
        // Set the Indian time zone
        ZoneId indianZone = ZoneId.of("Asia/Kolkata");

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now(indianZone);

        // Generate slots for the next 7 days
        for (int i = 0; i < 30; i++) {
            // Set the date to the current date + i days
            LocalDateTime currentDate = now.plusDays(i+1);

            // Set the time to 9:00 AM
            LocalDateTime currentSlot = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.of(9, 0));

            // Generate 9 slots one hour apart
            for (int j = 0; j < 9; j++) {
                // Add the current slot to the list
                slots.add(currentSlot);

                // Move to the next hour
                currentSlot = currentSlot.plusHours(1);
            }
        }

        return slots;
    }
}
