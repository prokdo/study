import { IsString, IsEmail, IsOptional, MinLength, IsEnum, IsDate, MaxDate } from 'class-validator';
import { Sex } from '../enums/sex.enum';

export class CreateUserDto {
  @IsEmail()
  @MinLength(2)
  email!: string;

  @IsString()
  @MinLength(6)
  password!: string;
}

export class UpdateUserDto {
  @IsOptional()
  @IsString()
  @MinLength(3)
  username?: string;

  @IsOptional()
  @IsEmail()
  @MinLength(2)
  email?: string;

  @IsOptional()
  @IsString()
  surname?: string;

  @IsOptional()
  @IsString()
  firstName?: string;

  @IsOptional()
  @IsString()
  lastName?: string;

  @IsOptional()
  @IsEnum(Sex)
  sex?: Sex;

  @IsOptional()
  @IsDate()
  @MaxDate(new Date())
  birthDate?: Date;
}
