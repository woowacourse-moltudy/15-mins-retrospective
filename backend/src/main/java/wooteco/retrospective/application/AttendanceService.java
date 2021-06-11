package wooteco.retrospective.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wooteco.retrospective.dao.attendance.AttendanceDao;
import wooteco.retrospective.dao.attendance.TimeDao;
import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.dto.AttendanceRequest;

@Service
public class AttendanceService {

    private final AttendanceDao attendanceDao;
    private final MemberDao memberDao;
    private final TimeDao timeDao;

    @Autowired
    public AttendanceService(AttendanceDao attendanceDao, MemberDao memberDao, TimeDao timeDao) {
        this.attendanceDao = attendanceDao;
        this.memberDao = memberDao;
        this.timeDao = timeDao;
    }

    public Attendance postAttendance(AttendanceRequest attendanceRequest) {
        if(attendanceDao.existSameTime(attendanceRequest.getMemberId(), attendanceRequest.getTimeId()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 시간입니다.");
        }

        Member member = memberDao.findById(attendanceRequest.getMemberId()).orElseThrow(RuntimeException::new);
        Time time = timeDao.findById(attendanceRequest.getTimeId()).orElseThrow(RuntimeException::new);
        Attendance attendance = new Attendance(attendanceRequest.getDate(), member, time);

        return attendanceDao.findById(attendanceDao.insert(attendance)).orElseThrow(RuntimeException::new);
    }

}
