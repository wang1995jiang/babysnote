package com.jpeng.demo.vioce.mini;

import java.util.List;

/**
 * Created by 王将 on 2018/4/13.
 */

public class Recong {

    /**
     * results_recognition : ["你好"]
     * origin_result : {"corpus_no":6543849921459984015,"err_no":0,"result":{"word":["你好","你好啊","你行","祢好","你郝","拟好"]},"sn":"bf52e66c-a7c5-4597-917b-a25be4984b2d","voice_energy":15592.068359375}
     * error : 0
     * best_result : 你好
     * result_type : final_result
     */

    private OriginResultBean origin_result;
    private int error;
    private String best_result="";
    private String result_type;
    private List<String> results_recognition;

    public OriginResultBean getOrigin_result() {
        return origin_result;
    }

    public void setOrigin_result(OriginResultBean origin_result) {
        this.origin_result = origin_result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getBest_result() {
        return best_result;
    }

    public void setBest_result(String best_result) {
        this.best_result = best_result;
    }

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public List<String> getResults_recognition() {
        return results_recognition;
    }

    public void setResults_recognition(List<String> results_recognition) {
        this.results_recognition = results_recognition;
    }

    public static class OriginResultBean {
        /**
         * corpus_no : 6543849921459984015
         * err_no : 0
         * result : {"word":["你好","你好啊","你行","祢好","你郝","拟好"]}
         * sn : bf52e66c-a7c5-4597-917b-a25be4984b2d
         * voice_energy : 15592.068359375
         */

        private long corpus_no;
        private int err_no;
        private ResultBean result;
        private String sn;
        private double voice_energy;

        public long getCorpus_no() {
            return corpus_no;
        }

        public void setCorpus_no(long corpus_no) {
            this.corpus_no = corpus_no;
        }

        public int getErr_no() {
            return err_no;
        }

        public void setErr_no(int err_no) {
            this.err_no = err_no;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public double getVoice_energy() {
            return voice_energy;
        }

        public void setVoice_energy(double voice_energy) {
            this.voice_energy = voice_energy;
        }

        public static class ResultBean {
            private List<String> word;

            public List<String> getWord() {
                return word;
            }

            public void setWord(List<String> word) {
                this.word = word;
            }
        }
    }
}
