DROP FUNCTION IF EXISTS piaoyou.similar;

delimiter $$

CREATE FUNCTION piaoyou.`similar`(text1 VARCHAR(255), text2 VARCHAR(255)) RETURNS double(6,5)
BEGIN
    DECLARE count     REAL;
    DECLARE result    REAL;
    DECLARE len1      INT;
    DECLARE len2      INT;
    
    SET count = 0;
    SET len1 = CHAR_LENGTH(text1);
    SET len2 = CHAR_LENGTH(text2);
    
    SET @i = 0;
    REPEAT 
      SET @i = @i + 1;
      IF locate(substring(text2,@i,1),text1) THEN
        SET count = count+1;
      END IF;
    UNTIL @i >= len2 END REPEAT;
    
    SET @i = 0;
    REPEAT 
      SET @i = @i + 1;
      IF locate(substring(text1,@i,1),text2) THEN
        SET count = count+1;
      END IF;
    UNTIL @i >= len1 END REPEAT;
    
    set result = (count)/(len1+len2);
    return(result);
  END$$
