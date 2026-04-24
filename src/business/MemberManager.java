package business;

import data.MemberData;
import model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MemberManager {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private List<Member> members;

    public MemberManager() {
        this.members = MemberData.loadAll();
    }

    public List<Member> getAllMembers() {
        return members;
    }

    public boolean addMember(String fullName, String email, String phone) {
        if (fullName == null || fullName.trim().isEmpty()) return false;
        if (email == null || email.trim().isEmpty()) return false;
        if (!isValidEmail(email)) return false;

        // ayni emaille kayit varsa eklemeyelim
        for (Member m : members) {
            if (m.getEmail().equalsIgnoreCase(email.trim())) {
                return false;
            }
        }

        int id = MemberData.nextId(members);
        Member m = new Member(id, fullName.trim(), email.trim(), phone == null ? "" : phone.trim());
        members.add(m);
        MemberData.saveAll(members);
        return true;
    }

    public boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public boolean deleteMember(int id) {
        Member toRemove = null;
        for (Member m : members) {
            if (m.getId() == id) {
                toRemove = m;
                break;
            }
        }
        if (toRemove != null) {
            members.remove(toRemove);
            MemberData.saveAll(members);
            return true;
        }
        return false;
    }

    public List<Member> search(String keyword) {
        List<Member> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return members;
        }
        String key = keyword.toLowerCase();
        for (Member m : members) {
            if (m.getFullName().toLowerCase().contains(key)
                    || m.getEmail().toLowerCase().contains(key)) {
                result.add(m);
            }
        }
        return result;
    }

    public Member findById(int id) {
        for (Member m : members) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    public void reload() {
        this.members = MemberData.loadAll();
    }
}
