export const formatWithTimezone = (date) => {
    // Tạo offset +07:00
    const timezoneOffset = 7; // GMT+7
    const adjustedDate = new Date(date.getTime() + timezoneOffset * 60 * 60 * 1000);
    
    // Format thành chuỗi và encode dấu +
    return encodeURIComponent(
      adjustedDate.toISOString()
        .replace('Z', '') // Bỏ Z cuối chuỗi
        .replace(/\.\d+/, '') // Bỏ milliseconds
        + `+07:00` // Thêm timezone
    );
};

export const decodeURIComponent = (encodedURIComponent) => {  
    return encodedURIComponent.replace(/%2B/g, '+').replace(/%3A/g, ':');
};