/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CraTestModule } from '../../../test.module';
import { ConfValueUpdateComponent } from 'app/entities/conf-value/conf-value-update.component';
import { ConfValueService } from 'app/entities/conf-value/conf-value.service';
import { ConfValue } from 'app/shared/model/conf-value.model';

describe('Component Tests', () => {
    describe('ConfValue Management Update Component', () => {
        let comp: ConfValueUpdateComponent;
        let fixture: ComponentFixture<ConfValueUpdateComponent>;
        let service: ConfValueService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ConfValueUpdateComponent]
            })
                .overrideTemplate(ConfValueUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ConfValueUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConfValueService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ConfValue(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.confValue = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ConfValue();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.confValue = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
