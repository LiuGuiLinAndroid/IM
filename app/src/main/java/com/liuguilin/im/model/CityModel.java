package com.liuguilin.im.model;

import java.util.List;

/**
 * FileName: CityModel
 * Founder: LiuGuiLin
 * Create Date: 2018/12/24 16:58
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class CityModel {


    private List<RootBean> root;

    public List<RootBean> getRoot() {
        return root;
    }

    public void setRoot(List<RootBean> root) {
        this.root = root;
    }

    public static class RootBean {
        /**
         * provinceCode : 110000
         * province : 北京市
         * cities : [{"cityCode":110100,"city":"北京市","superCode":110000,"counties":[{"countyCode":110101,"county":"东城区","superCode":110100},{"countyCode":110102,"county":"西城区","superCode":110100},{"countyCode":110103,"county":"崇文区","superCode":110100},{"countyCode":110104,"county":"宣武区","superCode":110100},{"countyCode":110105,"county":"朝阳区","superCode":110100},{"countyCode":110106,"county":"丰台区","superCode":110100},{"countyCode":110107,"county":"石景山区","superCode":110100},{"countyCode":110108,"county":"海淀区","superCode":110100},{"countyCode":110109,"county":"门头沟区","superCode":110100},{"countyCode":110111,"county":"房山区","superCode":110100},{"countyCode":110112,"county":"通州区","superCode":110100},{"countyCode":110113,"county":"顺义区","superCode":110100},{"countyCode":110114,"county":"昌平区","superCode":110100},{"countyCode":110115,"county":"大兴区","superCode":110100},{"countyCode":110116,"county":"怀柔区","superCode":110100},{"countyCode":110117,"county":"平谷区","superCode":110100}]}]
         */

        private int provinceCode;
        private String province;
        private List<CitiesBean> cities;

        public int getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(int provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<CitiesBean> getCities() {
            return cities;
        }

        public void setCities(List<CitiesBean> cities) {
            this.cities = cities;
        }

        public static class CitiesBean {
            /**
             * cityCode : 110100
             * city : 北京市
             * superCode : 110000
             * counties : [{"countyCode":110101,"county":"东城区","superCode":110100},{"countyCode":110102,"county":"西城区","superCode":110100},{"countyCode":110103,"county":"崇文区","superCode":110100},{"countyCode":110104,"county":"宣武区","superCode":110100},{"countyCode":110105,"county":"朝阳区","superCode":110100},{"countyCode":110106,"county":"丰台区","superCode":110100},{"countyCode":110107,"county":"石景山区","superCode":110100},{"countyCode":110108,"county":"海淀区","superCode":110100},{"countyCode":110109,"county":"门头沟区","superCode":110100},{"countyCode":110111,"county":"房山区","superCode":110100},{"countyCode":110112,"county":"通州区","superCode":110100},{"countyCode":110113,"county":"顺义区","superCode":110100},{"countyCode":110114,"county":"昌平区","superCode":110100},{"countyCode":110115,"county":"大兴区","superCode":110100},{"countyCode":110116,"county":"怀柔区","superCode":110100},{"countyCode":110117,"county":"平谷区","superCode":110100}]
             */

            private int cityCode;
            private String city;
            private int superCode;
            private List<CountiesBean> counties;

            public int getCityCode() {
                return cityCode;
            }

            public void setCityCode(int cityCode) {
                this.cityCode = cityCode;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getSuperCode() {
                return superCode;
            }

            public void setSuperCode(int superCode) {
                this.superCode = superCode;
            }

            public List<CountiesBean> getCounties() {
                return counties;
            }

            public void setCounties(List<CountiesBean> counties) {
                this.counties = counties;
            }

            public static class CountiesBean {
                /**
                 * countyCode : 110101
                 * county : 东城区
                 * superCode : 110100
                 */

                private int countyCode;
                private String county;
                private int superCode;

                public int getCountyCode() {
                    return countyCode;
                }

                public void setCountyCode(int countyCode) {
                    this.countyCode = countyCode;
                }

                public String getCounty() {
                    return county;
                }

                public void setCounty(String county) {
                    this.county = county;
                }

                public int getSuperCode() {
                    return superCode;
                }

                public void setSuperCode(int superCode) {
                    this.superCode = superCode;
                }
            }
        }
    }
}
