import { IsString, IsDate, IsNumber, Min, IsOptional, IsArray, ArrayUnique, MinLength } from 'class-validator';
import { Type } from 'class-transformer';

export class CreateEventDto {
  @IsString()
  @MinLength(5)
  name!: string;

  @IsString()
  @MinLength(10)
  description!: string;

  @IsDate()
  @Type(() => Date)
  date!: Date;

  @IsString()
  location!: string;

  @IsOptional()
  @IsNumber()
  @Min(1)
  capacity?: number;

  @IsOptional()
  @IsArray()
  @ArrayUnique()
  @IsString({ each: true })
  @MinLength(1, { each: true })
  tags?: string[];
}

export class UpdateEventDto {
  @IsOptional()
  @IsString()
  @MinLength(5)
  name?: string;

  @IsOptional()
  @IsString()
  @MinLength(10)
  description?: string;

  @IsOptional()
  @IsDate()
  @Type(() => Date)
  date?: Date;

  @IsOptional()
  @IsString()
  location?: string;

  @IsOptional()
  @IsNumber()
  @Min(1)
  capacity?: number;

  @IsOptional()
  @IsArray()
  @ArrayUnique()
  @IsString({ each: true })
  @MinLength(1, { each: true })
  tags?: string[];
}
