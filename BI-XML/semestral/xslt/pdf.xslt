<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:output method="xml" encoding="utf-8"/>
    <xsl:param name="num-format">US</xsl:param>
    <xsl:decimal-format name="US" />
    <xsl:variable name="num-pattern">#,###</xsl:variable>

    <xsl:template match="/">
        <fo:root font-family="Helvetica" font-size="11pt" language="eng">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="page" page-height="297mm" page-width="210mm" margin="1in">
                    <fo:region-body margin-bottom="10mm"/>
                    <fo:region-after extent="5mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="page">
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="center"><fo:page-number/></fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">

                    <fo:block font-size="36pt" font-weight="bold">
                        <fo:inline font-weight="bold">Countries:</fo:inline>
                    </fo:block>
                    <fo:list-block provisional-distance-between-starts="1em" font-size="24px" color="#0000FF">
                        <xsl:for-each select="/countries/country">
                            <fo:list-item>
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block>&#x2022;</fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block text-decoration="underline">
                                        <xsl:variable name="cname" select="@name" />
                                            <fo:inline text-decoration="underline">
                                                <fo:basic-link internal-destination="{$cname}">
                                                    <xsl:value-of select="@name" />
                                                </fo:basic-link>
                                            </fo:inline>
                                    </fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
                        </xsl:for-each>
                    </fo:list-block>

                    <xsl:apply-templates select="/countries/country" />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template match="/countries/country">
        <fo:block page-break-before="always" font-size="24pt" font-weight="bold" text-align="center" >
            <xsl:variable name="name" select="@name" />
            <fo:wrapper id="{$name}" />
            <xsl:value-of select="$name" />
        </fo:block>

        <fo:block text-align="center">
            <fo:external-graphic content-height="scale-to-fit" height="2.00in" content-width="2.00in" >
                <xsl:attribute name="src">
                    <xsl:value-of select="concat('../html/', government/flag/image)" />
                </xsl:attribute>
            </fo:external-graphic>
        </fo:block>

        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="introduction">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Introduction
        </fo:block>

        <xsl:for-each select="paragraph">
            <fo:block><xsl:value-of select="."/></fo:block>
        </xsl:for-each>

    </xsl:template>

    <xsl:template match="geography">
        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Geography
        </fo:block>

        <fo:block page-break-before="avoid">
            <!-- Location -->
            <xsl:apply-templates select="span[@align='top']" />

            <xsl:for-each select="area/@*">
                <xsl:choose>
                    <xsl:when test="name() = 'rank'">
                        <fo:block space-after="4px" />
                        <fo:inline font-weight="bold">Area ranking: </fo:inline><xsl:value-of select="." /> in the world <fo:block />
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:inline font-weight="bold"><xsl:value-of select="concat('Land - ', name())" />: </fo:inline> <xsl:value-of select="format-number(., $num-pattern, $num-format)" /> km2 <fo:block />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <fo:inline font-weight="bold">Area comparative: </fo:inline><xsl:value-of select="area"/> <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Boundaries: </fo:inline> (<xsl:value-of select="boundaries/@total" /> km with <xsl:value-of select="boundaries/@count" /> countries)
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="boundaries/boundary">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block><xsl:value-of select="@with" /> (<xsl:value-of select="@length" /> km) </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Maritime claims:</fo:inline>
            <xsl:choose>
                <xsl:when test="maritime_claims/maritime_claim">
                    <fo:list-block provisional-distance-between-starts="1em">
                        <xsl:for-each select="maritime_claims/maritime_claim">
                            <fo:list-item page-break-before="avoid">
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block>&#x2022;</fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block page-break-before="avoid">
                                        <fo:inline font-weight="bold">Type: </fo:inline> <xsl:value-of select="@type" />,
                                        <fo:inline font-weight="bold">Length: </fo:inline> <xsl:value-of select="." /> km
                                    </fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
                        </xsl:for-each>
                    </fo:list-block>
                </xsl:when>
                <xsl:otherwise> none</xsl:otherwise>
            </xsl:choose>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Elevation: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block><fo:inline font-weight="bold">Mean: </fo:inline> <xsl:value-of select="elevation/@mean" /> a.s.l.</fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block><fo:inline font-weight="bold">Lowest: </fo:inline> <xsl:value-of select="elevation/@lowestalt" /> a.s.l. (<xsl:value-of select="elevation/@lowest" />)</fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block><fo:inline font-weight="bold">Highest: </fo:inline> <xsl:value-of select="elevation/@highestalt" /> a.s.l. (<xsl:value-of select="elevation/@highest" />)</fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block space-after="4px" />

            <!-- Land -->
            <fo:inline font-weight="bold">Irrigated land: </fo:inline> <xsl:value-of select="land/@irrigated" /> km2 <fo:block />

            <fo:inline font-weight="bold">Land: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">Agricultural: </fo:inline> <xsl:value-of select="land/@agricultural" /> %
                            <fo:list-block provisional-distance-between-starts="1em">
                                <fo:list-item page-break-before="avoid">
                                    <fo:list-item-label end-indent="label-end()">
                                        <fo:block>&#x2022;</fo:block>
                                    </fo:list-item-label>
                                    <fo:list-item-body start-indent="body-start()">
                                        <fo:block><fo:inline font-weight="bold">Arable: </fo:inline> <xsl:value-of select="land/@arable" /> %</fo:block>
                                    </fo:list-item-body>
                                </fo:list-item>
                                <fo:list-item page-break-before="avoid">
                                    <fo:list-item-label end-indent="label-end()">
                                        <fo:block>&#x2022;</fo:block>
                                    </fo:list-item-label>
                                    <fo:list-item-body start-indent="body-start()">
                                        <fo:block><fo:inline font-weight="bold">Pasture: </fo:inline> <xsl:value-of select="land/@arablepasture" /> %</fo:block>
                                    </fo:list-item-body>
                                </fo:list-item>
                                <fo:list-item page-break-before="avoid">
                                    <fo:list-item-label end-indent="label-end()">
                                        <fo:block>&#x2022;</fo:block>
                                    </fo:list-item-label>
                                    <fo:list-item-body start-indent="body-start()">
                                        <fo:block><fo:inline font-weight="bold">Crops: </fo:inline> <xsl:value-of select="land/@arablecrops" /> %</fo:block>
                                    </fo:list-item-body>
                                </fo:list-item>
                            </fo:list-block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block><fo:inline font-weight="bold">Forest: </fo:inline> <xsl:value-of select="land/@forest" /> %</fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block><fo:inline font-weight="bold">Other: </fo:inline> <xsl:value-of select="land/@other" /> %</fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block space-after="4px" />

            <xsl:apply-templates select="span[not(@align)]" />
            <fo:block space-after="4px" />
            <xsl:apply-templates select="span[@align='right']" />
        </fo:block>
    </xsl:template>

    <xsl:template match="people_and_society">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            People and society
        </fo:block>

        <fo:block page-break-before="avoid">
            <fo:inline font-weight="bold">Population: </fo:inline> <xsl:value-of select="format-number(population/@count, $num-pattern, $num-format)" /> (ranking: <xsl:value-of select="population/@ranking" />)<fo:block />
            <fo:inline font-weight="bold">Nationality: </fo:inline> <xsl:value-of select="nationality/@noun" /> (adjective: <xsl:value-of select="nationality/@adjective" />)<fo:block />
            <fo:inline font-weight="bold">Ethnic groups: </fo:inline> <xsl:value-of select="ethnic_groups" /><fo:block space-after="4px" />

            <fo:inline font-weight="bold">Languages:</fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="languages/language">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <xsl:choose>
                                    <xsl:when test="@official"><fo:inline font-weight="bold"><xsl:value-of select="@name" /></fo:inline></xsl:when>
                                    <xsl:otherwise><xsl:value-of select="@name" /></xsl:otherwise>
                                </xsl:choose>

                                <xsl:if test="@percent">
                                    (<xsl:value-of select="@percent" /> %)
                                </xsl:if>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Religions:</fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="religions/religion">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@name"/></fo:inline>: <xsl:value-of select="@percent" /> %
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Age structure:</fo:inline>
            <fo:table table-layout="fixed" width="100%" border="solid" text-align="center" space-before="10px">
                <fo:table-body>
                    <fo:table-row border="solid" page-break-before="avoid" font-weight="bold">
                        <fo:table-cell border="solid"><fo:block>Min</fo:block></fo:table-cell>
                        <fo:table-cell border="solid"><fo:block>Max</fo:block></fo:table-cell>
                        <fo:table-cell border="solid"><fo:block>Percent</fo:block></fo:table-cell>
                        <fo:table-cell border="solid"><fo:block>Male</fo:block></fo:table-cell>
                        <fo:table-cell border="solid"><fo:block>Female</fo:block></fo:table-cell>
                    </fo:table-row>

                    <xsl:for-each select="age_structure/age">
                        <fo:table-row border="solid" page-break-before="avoid">
                            <fo:table-cell border="solid"><fo:block><xsl:value-of select="@min"/></fo:block></fo:table-cell>
                            <fo:table-cell border="solid"><fo:block><xsl:value-of select="@max"/></fo:block></fo:table-cell>
                            <fo:table-cell border="solid"><fo:block><xsl:value-of select="@percent"/> %</fo:block></fo:table-cell>
                            <fo:table-cell border="solid"><fo:block><xsl:value-of select="format-number(@male, $num-pattern, $num-format)"/></fo:block></fo:table-cell>
                            <fo:table-cell border="solid"><fo:block><xsl:value-of select="format-number(@female, $num-pattern, $num-format)"/></fo:block></fo:table-cell>
                        </fo:table-row>
                    </xsl:for-each>
                </fo:table-body>
            </fo:table>
            <fo:block space-after="10px" />

            <xsl:for-each select="dependency_ratio/@*">
                <fo:inline font-weight="bold"><xsl:value-of select="concat(upper-case(substring(name(),1,1)), substring(name(), 2))" /> dependency ratio: </fo:inline> <xsl:value-of select="." /> <fo:block />
            </xsl:for-each>
            <fo:block space-after="4px" />

            <xsl:for-each select="rate_per_thousand/@*">
                <fo:inline font-weight="bold"><xsl:value-of select="concat(upper-case(substring(name(),1,1)), substring(name(), 2))" /> rate: </fo:inline> <xsl:value-of select="." /> <xsl:value-of select="concat(' ', name())" />s / 1,000 population <fo:block />
            </xsl:for-each>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Sex ratio:</fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block><fo:inline font-weight="bold">Total: </fo:inline> <xsl:value-of select="sex_ratio/@total" /> male(s)/female</fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <xsl:for-each select="sex_ratio/age">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@min"/>-<xsl:value-of select="@max"/> years</fo:inline>: <xsl:value-of select="@male_over_female" /> male(s)/female
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Mother's mean age at first birth: </fo:inline> <xsl:value-of select="average_mother/@first_birth_age" /> years <fo:block />
            <fo:inline font-weight="bold">Maternal mortality: </fo:inline> <xsl:value-of select="average_mother/@mortality_rate" /> deaths / 100,000 live births <fo:block />
            <fo:inline font-weight="bold">Total fertility rate: </fo:inline> <xsl:value-of select="average_mother/@children" /> children / woman <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Drinking water source access: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="drinking_water/@*">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block><xsl:value-of select="name()" />: <xsl:value-of select="." /> % of population</fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Sanitation facility access: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="sanitation_facility_access/@*">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block><xsl:value-of select="name()" />: <xsl:value-of select="." /> % of population</fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Diseases: </fo:inline>
            <xsl:choose>
                <xsl:when test="not(diseases/disease)">none</xsl:when>
                <xsl:otherwise>
                    <fo:list-block provisional-distance-between-starts="1em">
                        <xsl:for-each select="diseases/disease">
                            <fo:list-item page-break-before="avoid">
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block>&#x2022;</fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block><fo:inline font-weight="bold"><xsl:value-of select="@type"/> disease(s)</fo:inline>: <xsl:value-of select="."/></fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
                        </xsl:for-each>
                    </fo:list-block>
                </xsl:otherwise>
            </xsl:choose>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Health expenditure: </fo:inline> <xsl:value-of select="health_expenditure" /> <fo:block space-after="4px" />

            <xsl:for-each select="statistics/statistic">
                <fo:inline font-weight="bold"><xsl:value-of select="." />: </fo:inline>
                <fo:list-block provisional-distance-between-starts="1em">
                    <xsl:for-each select="@*[name() != 'name']">
                        <fo:list-item page-break-before="avoid">
                            <fo:list-item-label end-indent="label-end()">
                                <fo:block>&#x2022;</fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="body-start()">
                                <fo:block><xsl:value-of select="name()" />: <xsl:value-of select="." /> % </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:for-each>
                </fo:list-block>
            </xsl:for-each>
        </fo:block>
        
    </xsl:template>

    <xsl:template match="government">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Government
        </fo:block>

        <fo:block page-break-before="avoid">
            <xsl:apply-templates select="span[@align='top']" />

            <fo:inline font-weight="bold">Administrative division: </fo:inline> <xsl:value-of select="divisions/@count" /><xsl:value-of select="concat(' ', divisions/@name)" />s
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Citizenship: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="citizenships/citizenship">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold">by <xsl:value-of select="@by" />: </fo:inline> <xsl:value-of select="." />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Executive power: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="executive/span">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <xsl:apply-templates select="." />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Legislative power: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="legislative/span">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <xsl:apply-templates select="." />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Judicial power: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="judicial/span">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <xsl:apply-templates select="." />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Flag: </fo:inline> <xsl:value-of select="flag/description" /> <fo:block />
            <fo:block space-after="4px" />

            <xsl:apply-templates select="span[@align='right']" />
        </fo:block>
        
    </xsl:template>

    <xsl:template match="economy">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Economy
        </fo:block>

        <fo:block page-break-before="avoid">
            <xsl:apply-templates select="span[@align='top']" /> <fo:block space-after="4px" />

            <xsl:apply-templates select="gdp_values/gdp_value" />
            <fo:block space-after="4px" />

            <xsl:apply-templates select="rates/rate" />
            <fo:block space-after="4px" />

            <xsl:for-each select="compositions/factor">
                <fo:inline font-weight="bold">GDP composition</fo:inline> (by <xsl:value-of select="@name"/>) :
                <fo:list-block provisional-distance-between-starts="1em">
                    <xsl:for-each select="composition">
                        <fo:list-item page-break-before="avoid">
                            <fo:list-item-label end-indent="label-end()">
                                <fo:block>&#x2022;</fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="body-start()">
                                <fo:block>
                                    <fo:inline font-weight="bold"><xsl:value-of select="replace(concat(upper-case(substring(@for,1,1)), substring(@for, 2)), '_', ' ')"/></fo:inline>: <xsl:value-of select="." /> %
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:for-each>
                </fo:list-block>
                <fo:block space-after="4px" />
            </xsl:for-each>

            <fo:inline font-weight="bold">Occupations</fo:inline> :
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="occupations/occupation">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@name"/></fo:inline>: <xsl:value-of select="." /> %
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <xsl:for-each select="eximports/eximport">
                <fo:inline font-weight="bold"><xsl:value-of select="@for" /> - <xsl:value-of select="@name" />: </fo:inline> <xsl:value-of select="." /> <fo:block />
            </xsl:for-each>
            <fo:block space-after="4px" />

            <xsl:apply-templates select="span[@align='right']" />
        </fo:block>

    </xsl:template>
    <xsl:template match="gdp_value">
        <fo:inline font-weight="bold">GDP - <xsl:value-of select="@type" />: </fo:inline> $<xsl:value-of select="." /> billion <xsl:if test="@ranking">(ranking <xsl:value-of select="@ranking" />)</xsl:if> <fo:block />
    </xsl:template>
    <xsl:template match="rate">
        <fo:inline font-weight="bold"><xsl:value-of select="replace(concat(upper-case(substring(@type,1,1)), substring(@type, 2)), '_', ' ')"/></fo:inline>: <xsl:value-of select="." /> % <fo:block />
    </xsl:template>

    <xsl:template match="energy">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Energy
        </fo:block>

        <fo:block page-break-before="avoid">
            <fo:inline font-weight="bold">People without electricity: </fo:inline> <xsl:value-of select="format-number(electrification/@without, $num-pattern, $num-format)" /> <fo:block />
            <fo:inline font-weight="bold">Electricity access: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="electrification/@*">
                    <xsl:if test="name() != 'without'">
                        <fo:list-item page-break-before="avoid">
                            <fo:list-item-label end-indent="label-end()">
                                <fo:block>&#x2022;</fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="body-start()">
                                <fo:block>
                                    <fo:inline font-weight="bold"><xsl:value-of select="name()" />: </fo:inline>
                                    <xsl:value-of select="." /> %
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:if>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Electricity production: </fo:inline> <xsl:value-of select="electricity/@production" /> billion kWh (ranking <xsl:value-of select="electricity/@production_rank" />) <fo:block />
            <fo:inline font-weight="bold">Electricity consumption: </fo:inline> <xsl:value-of select="electricity/@consumption" /> billion kWh (ranking <xsl:value-of select="electricity/@consumption_rank" />) <fo:block />
            <fo:inline font-weight="bold">Electricity - export: </fo:inline> <xsl:value-of select="electricity/@exports" /> billion kWh <fo:block />
            <fo:inline font-weight="bold">Electricity - import: </fo:inline> <xsl:value-of select="electricity/@imports" /> billion kWh <fo:block />
            <fo:inline font-weight="bold">Installed generating capacity: </fo:inline> <xsl:value-of select="electricity/@generating" /> billion kWh <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Electricity - by source:</fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="electricity_source/@*">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="replace(concat(upper-case(substring(name(),1,1)), substring(name(), 2)), '_', ' ')" />: </fo:inline> <xsl:value-of select="."/> %
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-size="15pt" font-weight="bold">Resources:</fo:inline><fo:block space-after="6px" />

            <xsl:for-each select="resource_list/resource">
                <fo:inline font-weight="bold"><xsl:value-of select="@name" /> </fo:inline>:
                <fo:list-block provisional-distance-between-starts="1em">
                    <xsl:variable name="unit" select="@unit" />
                    <xsl:variable name="runit" select="@runit" />
                    <xsl:for-each select="@*">
                        <xsl:choose>
                            <xsl:when test="name() = 'unit' or name() = 'runit' or name() = 'name'" />
                            <xsl:otherwise>
                                <fo:list-item page-break-before="avoid">
                                    <fo:list-item-label end-indent="label-end()">
                                        <fo:block>&#x2022;</fo:block>
                                    </fo:list-item-label>
                                    <fo:list-item-body start-indent="body-start()">
                                        <fo:block>
                                            <fo:inline font-weight="bold"><xsl:value-of select="name()" /></fo:inline>:
                                            <xsl:choose>
                                                <xsl:when test="name() = 'reserves'">
                                                    <xsl:value-of select="concat(format-number(., $num-pattern, $num-format), ' ', $runit)"/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of select="concat(format-number(., $num-pattern, $num-format), ' ', $unit)"/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </fo:block>
                                    </fo:list-item-body>
                                </fo:list-item>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                </fo:list-block>
                <fo:block space-after="4px" />
            </xsl:for-each>
        </fo:block>

    </xsl:template>

    <xsl:template match="communications">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Communications
        </fo:block>
        
        <fo:block page-break-before="avoid">
            <xsl:for-each select="phones/phone">
                <fo:inline font-weight="bold">Telephones - <xsl:value-of select="@type" />: </fo:inline>
                <fo:list-block provisional-distance-between-starts="1em">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold">total subscriptions: </fo:inline><xsl:value-of select="format-number(@total, $num-pattern, $num-format)"/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold">subscriptions per 100 habitants: </fo:inline><xsl:value-of select="@per_100"/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold">country ranking: </fo:inline><xsl:value-of select="@ranking"/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </fo:list-block>
                <fo:block space-after="4px" />
            </xsl:for-each>

            <fo:inline font-weight="bold">Phone systems: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="phone_systems/phone_system">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@type"/>: </fo:inline><xsl:value-of select="." />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <xsl:apply-templates select="span[@align='right']" />

            <fo:inline font-weight="bold">Internet code: </fo:inline> .<xsl:value-of select="internet/@code" /> <fo:block />
            <fo:inline font-weight="bold">Internet users: </fo:inline> <xsl:value-of select="format-number(internet/@users, $num-pattern, $num-format)" /> <fo:block />
            <fo:inline font-weight="bold">Percent of population with access to internet: </fo:inline> <xsl:value-of select="internet/@percent" /> % (ranking <xsl:value-of select="internet/@ranking" />)
        </fo:block>

    </xsl:template>

    <xsl:template match="transportation">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Transportation
        </fo:block>
        
        <fo:block page-break-before="avoid">
            <xsl:apply-templates select="span[@align='top']" />

            <fo:inline font-weight="bold">Air transportation: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <xsl:for-each select="air/air_unit">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@name"/>: </fo:inline> <xsl:value-of select="format-number(., $num-pattern, $num-format)" />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
                <xsl:for-each select="air/span">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@name"/>: </fo:inline> <xsl:value-of select="." />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />
            <fo:list-block provisional-distance-between-starts="1em">
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">Registration prefix: </fo:inline> <xsl:value-of select="air/registration_prefix" />
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">Position in the world: </fo:inline> <xsl:value-of select="air/ranking" />
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">Heliports: </fo:inline> <xsl:value-of select="air/heliports" />
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Airports: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">Total: </fo:inline> <xsl:value-of select="air/airports/@total" />
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <xsl:for-each select="air/airports/airport">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@min" /> to <xsl:value-of select="@max" /> m</fo:inline>: <xsl:value-of select="." />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Rails: </fo:inline>
            <xsl:choose>
                <xsl:when test="rails/@total=0">
                    none
                </xsl:when>
                <xsl:otherwise>
                    <fo:list-block provisional-distance-between-starts="1em">
                        <fo:list-item page-break-before="avoid">
                            <fo:list-item-label end-indent="label-end()">
                                <fo:block>&#x2022;</fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="body-start()">
                                <fo:block>
                                    <fo:inline font-weight="bold">total: </fo:inline> <xsl:value-of select="format-number(rails/@total, $num-pattern, $num-format)" /> km
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                        <xsl:if test="rails/@ranking">
                            <fo:list-item page-break-before="avoid">
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block>&#x2022;</fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block>
                                        <fo:inline font-weight="bold">ranking: </fo:inline> <xsl:value-of select="rails/@ranking" />
                                    </fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
                        </xsl:if>

                        <xsl:for-each select="rails/rail">
                            <fo:list-item page-break-before="avoid">
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block>&#x2022;</fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block>
                                        <fo:inline font-weight="bold"><xsl:value-of select="@gauge" /> gauge</fo:inline>: <xsl:value-of select="format-number(., $num-pattern, $num-format)" /> km
                                    </fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
                        </xsl:for-each>
                    </fo:list-block>
                </xsl:otherwise>
            </xsl:choose>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Roads: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">total: </fo:inline> <xsl:value-of select="format-number(roads/@total, $num-pattern, $num-format)" /> km
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <xsl:if test="roads/@ranking">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold">ranking: </fo:inline> <xsl:value-of select="roads/@ranking" />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:if>

                <xsl:for-each select="roads/road">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold"><xsl:value-of select="@type" /></fo:inline>: <xsl:value-of select="format-number(., $num-pattern, $num-format)" /> km
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
            <fo:block space-after="4px" />

            <fo:inline font-weight="bold">Water: </fo:inline>
            <fo:list-block provisional-distance-between-starts="1em">
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">Length: </fo:inline> <xsl:value-of select="format-number(water/length, $num-pattern, $num-format)" /> km
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <xsl:if test="water/ranking">
                    <fo:list-item page-break-before="avoid">
                        <fo:list-item-label end-indent="label-end()">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="body-start()">
                            <fo:block>
                                <fo:inline font-weight="bold">Ranking: </fo:inline> <xsl:value-of select="water/ranking" />
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:if>
                <fo:list-item page-break-before="avoid">
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:inline font-weight="bold">Ports: </fo:inline> <xsl:value-of select="water/ports" />
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
        </fo:block>

    </xsl:template>

    <xsl:template match="military_and_security">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Military and security
        </fo:block>
        
        <fo:block page-break-before="avoid">
            <xsl:apply-templates select="span" />
        </fo:block>

    </xsl:template>

    <xsl:template match="terrorism">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Terrorism
        </fo:block>

        <fo:block page-break-before="avoid">
            <fo:inline font-weight="bold">Terrorist group(s): </fo:inline> <xsl:value-of select="." />
        </fo:block>

    </xsl:template>

    <xsl:template match="transnational_issues">

        <fo:block font-size="16pt" font-weight="bold" space-before="10px" space-after="10px">
            Transnational issues
        </fo:block>
        
        <fo:block page-break-before="avoid">
            <xsl:apply-templates select="span" />

            <fo:inline font-weight="bold">Refugees: </fo:inline>
            <xsl:choose>
                <xsl:when test="refugees/refugee">
                    <fo:list-block provisional-distance-between-starts="1em">
                        <xsl:for-each select="refugees/refugee">
                            <fo:list-item page-break-before="avoid">
                                <fo:list-item-label end-indent="label-end()">
                                    <fo:block>&#x2022;</fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="body-start()">
                                    <fo:block>
                                        <fo:inline font-weight="bold"><xsl:value-of select="@from"/></fo:inline>: <xsl:value-of select="format-number(@count, $num-pattern, $num-format)" />
                                    </fo:block>
                                </fo:list-item-body>
                            </fo:list-item>
                        </xsl:for-each>
                    </fo:list-block>
                </xsl:when>
                <xsl:otherwise>none</xsl:otherwise>
            </xsl:choose>
        </fo:block>

    </xsl:template>

    <xsl:template match="span">
        <fo:inline font-weight="bold"><xsl:value-of select="@name"/>: </fo:inline> <xsl:value-of select="." /> <fo:block /> <xsl:if test="@newline='1'"><fo:block space-after="4px" /></xsl:if>
    </xsl:template>

</xsl:stylesheet>