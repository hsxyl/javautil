package exception;

import java.util.Random;

/**
 * @author xushenbao
 * @desc 记录型异常
 * @create 2020/8/6
 */
public class RecordException extends RuntimeException {

//region field
    private static final Random random = new Random();
    private static final Menu menu = new Menu();
    private final int uuid = random.nextInt();
    /**
     * 详细记录,用于保存debug信息
     */
    private String record = "";
    /**
     * 提示信息
     */
    private String hint = "";
// endregion

// region 构造
    private RecordException(String hint, String record, Throwable throwable) {
        super(throwable);
        appendHint(hint);
        appendRecord(record);
    }

    public static Menu build() {
        return menu;
    }
// endregion

// region public方法

    /**
     * 添加记录
     *
     * @param record 记录
     */
    public void appendRecord(String record) {
        if (record == null || record.length() == 0) {
            return;
        }
        this.record = this.record + "\n" + record;
    }

    /**
     * 获取uuid
     *
     * @return 返回唯一标识
     */
    public int getUuid() {
        return this.uuid;
    }

    public void appendHint(String hint) {
        if (hint == null || hint.length() == 0) {
            return;
        }
        if (this.hint != null && this.hint.length() != 0) {
            this.hint = hint + "\n" + this.hint;
        } else {
            this.hint = hint;
        }
    }

    public String getHint() {
        return "错误信息uuid:" + uuid + "\n" + hint;
    }

    public boolean isRecord() {
        return record != null && record.length() != 0;
    }


    @Override
    public String getLocalizedMessage() {
        return "uuid:" + uuid + "\nhint:" + hint + "\nmessage:" + getMessage() + "\nrecord:" + record;
    }
// endregion

// region 工厂
    private interface Build {
        RecordException build();
    }

    abstract static class Builder implements Build {
        String hint;
        String record;
        Throwable throwable;

        @Override
        public RecordException build() {
            return new RecordException(hint, record, throwable);
        }
    }

    public static class Menu {
        private Menu() {
        }

        public static CommonBuilder record(String record) {
            return new CommonBuilder().record(record);
        }

        public static CommonBuilder hint(String hint) {
            return new CommonBuilder().hint(hint);
        }
    }

    public static class CommonBuilder extends Builder {
        private CommonBuilder() {
        }

        public CommonBuilder record(String record) {
            super.record = record;
            return this;
        }

        public CommonBuilder hint(String hint) {
            this.hint = hint;
            return this;
        }
    }
// endregion

}
